package net.sakz.docker.bean;

import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.ImageSearchResult;

import net.sakz.docker.Constants;

public class ConvertBeanToVO {

    private ConvertBeanToVO() {
        throw new UnsupportedOperationException();
    }

    public static ImageVO image(Image image) {
        ImageVO imageVO = new ImageVO();
        imageVO.setCreated(DateFormatUtils.format(new Date(Long.valueOf(image.created())*1000), Constants.DEFAULT_DATAFORMAT_STR));
        imageVO.setSize(FileUtils.byteCountToDisplaySize(image.size()));
        imageVO.setVirtualSize(FileUtils.byteCountToDisplaySize(image.virtualSize()));
        imageVO.setParentId(image.parentId());
        imageVO.setId(image.id());
        imageVO.setRepoTags(image.repoTags().toArray(new String[0]));
        return imageVO;
    }
    
    public static ContainerVO container(Container container) {
    	ContainerVO imageVO = new ContainerVO();
        imageVO.setCreated(DateFormatUtils.format(new Date(Long.valueOf(container.created())*1000), Constants.DEFAULT_DATAFORMAT_STR));
        imageVO.setCommand(container.command());
        imageVO.setId(container.id());
        imageVO.setImage(container.image());
        imageVO.setPorts(container.portsAsString());
        imageVO.setStatus(container.status());
        return imageVO;
    }
    public static SearchItemVO searchItem(ImageSearchResult item) {
        SearchItemVO itemVO = new SearchItemVO();
        itemVO.setDescription(item.description());
        itemVO.setName(item.name());
        itemVO.setStarCount(item.starCount());
        itemVO.setOfficial(Official.getText(item.official()));
        return itemVO;
    }
}
