package net.sakz.docker;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ExecCreation;

import net.sakz.docker.bean.ContainerVO;
import net.sakz.docker.bean.ConvertBeanToVO;

/**
 * docker 容器管理实现
 * @author hunter
 *
 */
final class DockerContainerOptImpl implements IDockerContainerOpt {
	private DefaultDockerClient client;
	private ConcurrentHashMap<String, BashExecSession> execSessionMap=new ConcurrentHashMap<>();
	
	DockerContainerOptImpl(DefaultDockerClient client) {
		this.client=client;
	}
	
	@Override
	public List<ContainerVO> getContainer() throws Exception{
		List<Container> containers=client.listContainers();
        List<ContainerVO> imageVOs = new ArrayList<>(containers.size());
        for (Container image : containers) {
            imageVOs.add(ConvertBeanToVO.container(image));
        }
        return imageVOs;
	}

	@Override
	public String createBashExec(String containerId) throws Exception {
		ExecCreation execCreation=client.execCreate(containerId,Constants.EXEC_CMD_ONE,
                DockerClient.ExecCreateParam.attachStdin(), DockerClient.ExecCreateParam.attachStdout(), 
                DockerClient.ExecCreateParam.attachStderr(),
                DockerClient.ExecCreateParam.tty(true));
		return  execCreation.id();
	}

	@Override
	public void connectBashExec(String containerId, String execId,BashExecEvent event) throws Exception {
		Field f=client.getClass().getDeclaredField("uri");
		f.setAccessible(true);
		URI u=(URI) f.get(client);
        Socket socket=new Socket(u.getHost(),u.getPort());
        socket.setKeepAlive(true);
        OutputStream out = socket.getOutputStream();
        StringBuffer pw = new StringBuffer();
        pw.append("POST /exec/"+execId+"/start HTTP/1.1\r\n");
        pw.append("Host: "+u.getHost()+":"+u.getPort()+"\r\n");
        pw.append("User-Agent: Docker-Client\r\n");
        pw.append("Content-Type: application/json\r\n");
        pw.append("Connection: Upgrade\r\n");
        JSONObject js=new JSONObject();
        js.put("Detach",false);
        js.put("Tty",true);
        String json=js.toJSONString();
        pw.append("Content-Length: "+json.length()+"\r\n");
        pw.append("Upgrade: tcp\r\n");
        pw.append("\r\n");
        pw.append(json);
        out.write(pw.toString().getBytes("UTF-8"));
        out.flush();
        
        BashExecSession outPutThread=new BashExecSession(socket,u.toString(),containerId,execId,event);
        execSessionMap.putIfAbsent(execId,outPutThread);
        outPutThread.start();
	}

	@Override
	public void resizeTty( String width, String height, String execId) throws Exception {
		client.execResizeTty(execId,Integer.parseInt(height),Integer.parseInt(width));
	}

	@Override
	public void intputBashExec(String execId,byte[] bytes) throws Exception {
		BashExecSession execSession=execSessionMap.get(execId);
        if(execSession!=null&&execSession.getSocket()!=null) 
        {
            OutputStream out = execSession.getSocket().getOutputStream();
            out.write(bytes);
            out.flush();
        }
	}


	@Override
	public void closeBashExec(String execId)  {
		BashExecSession execSession=execSessionMap.get(execId);
        if(execSession!=null){
            execSession.interrupt();
            try {
            	execSession.getSocket().getInputStream().close();
            }catch (Exception e) {
				// TODO: handle exception
			}
            try {
            	execSession.getSocket().getOutputStream().close();
            }catch (Exception e) {}
            try {
            	execSession.getSocket().close();
            }catch (Exception e) {
            	
			}
            execSessionMap.remove(execId);
            execSession=null;
        }
		
	}

	@Override
	public void closeBashExec() {
		while(execSessionMap.keys().hasMoreElements()) {
			String key=execSessionMap.keys().nextElement();
			closeBashExec(key);
		}
		
	}

}
