package net.sakz.docker;

import java.util.List;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.messages.Info;
import com.spotify.docker.client.messages.Version;

import net.sakz.docker.bean.ContainerVO;
import net.sakz.docker.bean.ImageVO;
import net.sakz.docker.bean.SearchItemVO;
import net.sakz.docker.web.service.RedisService;


/**
 * docker client实现
 * @author hunter
 *
 */
final  class DockerClientOptImpl implements IDockerClientOpt {
    
	private DefaultDockerClient client;
    /**
     * 镜像管理
     */
    private IDockerImageOpt imageDelegate;
    /**
     * 容器管理
     */
    private IDockerContainerOpt containerDelegate;

    
    DockerClientOptImpl(RedisService redisService,String serverUrl){
    	this(redisService,DefaultDockerClient.builder().uri(serverUrl).build());
    }
    
    DockerClientOptImpl(RedisService redisService,DefaultDockerClient client){
    	this.client=client;
    	this.imageDelegate=new DockerImageOptImpl(redisService, client);
    	this.containerDelegate=new DockerContainerOptImpl(client);
    }
    


	public void setImageDelegate(IDockerImageOpt imageDelegate) {
		this.imageDelegate = imageDelegate;
	}

	public void setContainerDelegate(IDockerContainerOpt containerDelegate) {
		this.containerDelegate = containerDelegate;
	}



	@Override
    public Info getInfo() throws Exception {
    	return client.info();
    }


    @Override
    public Version getVersion() throws Exception{
        return client.version();
    }

    @Override
    public List<ImageVO> getImages() throws Exception{
        return imageDelegate.getImages();
    }

    @Override
    public void removeImage(String imageId) throws Exception {
        imageDelegate.removeImage(imageId);
    }

    @Override
    public List<SearchItemVO> searchImages(String term) throws Exception{
        return imageDelegate.searchImages(term);
    }

    @Override
    public void pullImage(String repository) throws Exception{
    	client.pull(repository); 
    }

	@Override
	public List<ContainerVO> getContainer() throws Exception{
		return containerDelegate.getContainer();
	}

	@Override
	public String createBashExec(String containerId) throws Exception {
		return containerDelegate.createBashExec(containerId);
	}

	@Override
	public void connectBashExec(String containerId, String execId,BashExecEvent event) throws Exception {
		containerDelegate.connectBashExec(containerId, execId, event);
	}

	@Override
	public void resizeTty(String width, String height, String execId) throws Exception {
		containerDelegate.resizeTty(width, height, execId);
	}

	@Override
	public void intputBashExec(String execId,byte[] bytes) throws Exception {
		containerDelegate.intputBashExec(execId, bytes);
	}


	@Override
	public void closeBashExec(String execId)  {
		containerDelegate.closeBashExec(execId);
	}

	@Override
	public void closeBashExec() {
		containerDelegate.closeBashExec();
	}

	@Override
	public void destroyClient() {
		closeBashExec();
	}
}