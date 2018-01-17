package net.sakz.docker;

import java.net.Socket;
import java.util.Date;

import net.sakz.docker.IDockerContainerOpt.BashExecEvent;


/**
 * websocket的session. 输出信息
 * 
 * <p>使用线程输出，方式流等待卡住</p>
 * @author will
 */
public class BashExecSession extends Thread {
    private BashExecEvent session;
    private String dockerUrl, containerId,  execId;
    private Socket socket;
    
    public BashExecSession(Socket socket,String dockerUrl, String containerId,String execId, BashExecEvent session){
        super("OutPut:"+new Date().getTime());
        this.dockerUrl=dockerUrl;
        this.containerId=containerId;
        this.execId=execId;
        this.session=session;
        this.socket=socket;
    }
    public Socket getSocket() {
		return socket;
	}
    @Override
    public String toString() {
    	StringBuffer sb=new StringBuffer();
    	sb.append("docker url:").append(dockerUrl).append("\r\n");
    	sb.append("container id:").append(containerId).append("\r\n");
    	sb.append("bash id:").append(execId).append("\r\n");
    	return sb.toString();
    }
    public void run() {
    	try{
    		boolean isClearFlag=false;
	        byte[] bytes=new byte[10240];
	        StringBuffer returnMsg=new StringBuffer();
	        while(!this.isInterrupted()){
	            int n = socket.getInputStream().read(bytes);
	            String msg=new String(bytes,0,n);
	            if(isClearFlag) {
	            	session.onReceive(msg);
	            }else {
	            	returnMsg.append(msg);
		            if(returnMsg.indexOf("\r\n\r\n")!=-1){
		                session.onReceive(returnMsg.substring(returnMsg.indexOf("\r\n\r\n")+4,returnMsg.length()));
		                session.onReceive(toString());
		                isClearFlag=true; 
		            }
	            }
	            bytes=new byte[10240];
	        }
    	}catch (Exception e) {
    		
		}
    }
}
