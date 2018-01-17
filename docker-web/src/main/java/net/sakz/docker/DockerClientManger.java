package net.sakz.docker;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;

import net.sakz.docker.web.service.RedisService;

/**
 * docker client实现
 * @author hunter
 *
 */
public  class DockerClientManger  {
	private Logger log=Logger.getLogger(DockerClientOptImpl.class);
	/**
	 * docker客户端对象池
	 */
	private ConcurrentReferenceHashMap<String, IDockerClientOpt> map=new ConcurrentReferenceHashMap<>();

	private RedisService redisService;
	
	
	private DockerClientManger() {
		
	}
	/**
     * 获取Url 对应的唯一的client
     * 该方法保证一个url只有一个client
     * @param serverUrl
     * @return
     * @throws Exception
     */
    public IDockerClientOpt getOnlyClientByUrl(String serverUrl) throws Exception {
    	IDockerClientOpt client=new DockerClientOptImpl(redisService, serverUrl);
    	IDockerClientOpt putClient=map.putIfAbsent(serverUrl,client);
    	if(putClient!=null) {
    		log.info("获取URL:"+serverUrl+",client:"+putClient);
    		return putClient;
    	}
    	log.info("获取URL:"+serverUrl+",client:"+client);
    	return client;
    }
    /**
     * 
     * @param serverUrl
     * @throws Exception 
     */
    public void destroyClient(String serverUrl) throws Exception  {
    	/**
    	 * 关闭所有的bash
    	 */
    	getOnlyClientByUrl(serverUrl).destroyClient();
    	map.remove(serverUrl);
    }
    
	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}
	
    public static DockerClientManger getInstance() {
        return Holder.INSTANCE;
    }	
    
    private static final class Holder {
        private static DockerClientManger INSTANCE = new DockerClientManger();
    }
}