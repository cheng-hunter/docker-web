package net.sakz.docker.web.handler;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.sakz.docker.DockerClientManger;
import net.sakz.docker.IDockerContainerOpt.BashExecEvent;


/**
 * 连接容器执行命令.
 * @author will
 */
@Component
public class ContainerExecWSHandler extends TextWebSocketHandler {
	
    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        //获得传参
    	String serverUrl=session.getAttributes().get("serverUrl").toString();
        String containerId=session.getAttributes().get("containerId").toString();
        String width=session.getAttributes().get("width").toString();
        String height=session.getAttributes().get("height").toString();
        //创建bash
        
        String execId = DockerClientManger.getInstance().getOnlyClientByUrl(serverUrl)
        		.createBashExec(containerId);
        //保存本次bash  exec Id
        session.getAttributes().put("execId", execId);
        //连接bash
        DockerClientManger.getInstance().getOnlyClientByUrl(serverUrl).connectBashExec(containerId,execId,new BashExecEvent() {
			
			@Override
			public void onReceive(String msg) {
				try {
					session.sendMessage(new TextMessage(msg));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        //修改tty大小
        DockerClientManger.getInstance().getOnlyClientByUrl(serverUrl).resizeTty(width, height, execId);
    }

    /**
     * websocket关闭后关闭线程.
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception  {
        String execId=session.getAttributes().get("execId").toString();
        DockerClientManger.getInstance().getOnlyClientByUrl(session.getAttributes().get("serverUrl").toString())
        .closeBashExec(execId);
    }

    /**
     * 获得先输入.
     * @param session
     * @param message 输入信息
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	DockerClientManger.getInstance().getOnlyClientByUrl(session.getAttributes().get("serverUrl").toString()).intputBashExec(session.getAttributes().get("execId").toString(), message.asBytes());
    }


}
