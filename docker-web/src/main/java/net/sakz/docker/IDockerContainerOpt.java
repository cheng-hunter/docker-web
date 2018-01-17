package net.sakz.docker;

import java.io.IOException;
import java.util.List;

import net.sakz.docker.bean.ContainerVO;

/**
 * docker容器操作
 * @author hunter
 *
 */
public interface IDockerContainerOpt {
	public interface BashExecEvent{
		/**
		 * 获取到bash输出
		 * @param msg
		 */
		void  onReceive(String msg);
	}
    /**
     * 获取所有的容器
     * @param url
     * @return
     * @throws Exception
     */
    List<ContainerVO> getContainer() throws Exception;
	 /**
     * 创建bash.
     * @param ip 宿主机ip地址
     * @param containerId 容器id
     * @return 命令id
     * @throws Exception
     */
    String createBashExec(String containerId) throws Exception ;
    
    /**
     * 连接bash.
     * @param ip 宿主机ip地址
     * @param execId 命令id
     * @param event    docker容器bash输出事件
     * @param dockerUrl docker地址
     * @return 连接的socket
     * @throws IOException
     */
    void connectBashExec(String containerId,String execId,BashExecEvent event) throws Exception;
    /**
     * 输入信息到bash
     * @param bytes
     * @throws Exception
     */
    void  intputBashExec(String execId,byte[] bytes) throws Exception;
    
    /**
     * 关闭断开bash 
     * @param execId bash  execId
     * @throws Exception
     */
    void  closeBashExec(String execId) ;
    /**
     * 关闭所有的bash
     */
    void  closeBashExec() ;
    /**
     * 修改tty大小.
     * @param ip
     * @param width
     * @param height
     * @param execId
     * @throws Exception
     */
    void resizeTty(final String width, final String height,final String execId) throws Exception ;

}
