package net.sakz.docker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.ImageSearchResult;

import net.sakz.docker.bean.ConvertBeanToVO;
import net.sakz.docker.bean.ImageVO;
import net.sakz.docker.bean.SearchItemVO;
import net.sakz.docker.web.service.RedisService;

/**
 * docker镜像管理默认实现
 * @author hunter
 *
 */
final class DockerImageOptImpl implements IDockerImageOpt{

	private RedisService redisService;
	private DefaultDockerClient client;
	DockerImageOptImpl(RedisService redisService,DefaultDockerClient client){
		this.client=client;
		this.redisService=redisService;
	}
	 @Override
	    public List<ImageVO> getImages() throws Exception{
	        List<Image> images = client.listImages();
	        List<ImageVO> imageVOs = new ArrayList<>(images.size());
	        for (Image image : images) {
	            imageVOs.add(ConvertBeanToVO.image(image));
	        }
	        return imageVOs;
	    }

	    @Override
	    public void removeImage(String imageId) throws Exception {
	        client.removeImage(imageId, true, true);
	    }

	    @Override
	    public List<SearchItemVO> searchImages(String term) throws Exception{
	        final String key = "search:image:term:" + DigestUtils.md5Hex(term);
	        if (!redisService.exist(key)) {
	            List<ImageSearchResult> items = client.searchImages(term);
	            List<SearchItemVO> itemVOs = new ArrayList<SearchItemVO>(items.size());
	            for (ImageSearchResult item : items) {
	                itemVOs.add(ConvertBeanToVO.searchItem(item));
	            }
	            redisService.setEx(key, JSON.toJSONString(itemVOs), 3600L);
	        }
	        return JSON.parseArray(redisService.get(key), SearchItemVO.class);
	    }

	    @Override
	    public void pullImage(String repository) throws Exception{
	    	client.pull(repository); 
	    }

	
}
