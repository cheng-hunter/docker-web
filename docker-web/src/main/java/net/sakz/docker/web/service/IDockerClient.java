package net.sakz.docker.web.service;

import java.io.IOException;
import java.util.List;

import com.spotify.docker.client.messages.Info;
import com.spotify.docker.client.messages.Version;

import net.sakz.docker.bean.ContainerVO;
import net.sakz.docker.bean.ImageVO;
import net.sakz.docker.bean.SearchItemVO;

public interface IDockerClient {
	public interface BashExecEvent{
		/**
		 * 获取到bash输出
		 * @param msg
		 */
		void  onReceive(String msg);
	}
	
	/**
	 * 获取信息
	 * @param url
	 * @return
	 * @throws Exception
	 */
    Info getInfo(String url) throws Exception;
    /**
     * 获取版本信息
     * @param url
     * @return
     * @throws Exception
     */
    Version getVersion(String url) throws Exception;
    /**
     * 获取镜像列表
     * @param url
     * @return
     * @throws Exception
     */
    List<ImageVO> getImages(String url) throws Exception;
    /**
     * 获取所有的容器
     * @param url
     * @return
     * @throws Exception
     */
    List<ContainerVO> getContainer(String url) throws Exception;
    /**
     * 搜索镜像
     * @param term
     * @param url
     * @return
     * @throws Exception
     */
    List<SearchItemVO> searchImages(String term,String url) throws Exception;
    /**
     * 移除镜像
     * @param imageId
     * @param url
     * @throws Exception
     */
    void removeImage(String imageId,String url) throws Exception;
    /**
     * 下载镜像
     * @param repository
     * @param url
     * @throws Exception
     */
    void pullImage(String repository,String url) throws Exception;
    /**
     * 创建bash.
     * @param ip 宿主机ip地址
     * @param containerId 容器id
     * @return 命令id
     * @throws Exception
     */
    String createBashExec(String dockerUrl, String containerId) throws Exception ;
    
    /**
     * 连接bash.
     * @param ip 宿主机ip地址
     * @param execId 命令id
     * @param event    docker容器bash输出事件
     * @param dockerUrl docker地址
     * @return 连接的socket
     * @throws IOException
     */
    void connectBashExec(String dockerUrl, String containerId,String execId,BashExecEvent event) throws Exception;
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
     * 修改tty大小.
     * @param ip
     * @param width
     * @param height
     * @param execId
     * @throws Exception
     */
    void resizeTty(String docekrUrl, final String width, final String height,final String execId) throws Exception ;
}
