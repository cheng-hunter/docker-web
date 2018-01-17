package net.sakz.docker;

import java.util.List;

import net.sakz.docker.bean.ImageVO;
import net.sakz.docker.bean.SearchItemVO;

/**
 * docker 容器镜像管理
 * @author hunter
 *
 */
public interface IDockerImageOpt {
    /**
     * 获取镜像列表
     * @param url
     * @return
     * @throws Exception
     */
    List<ImageVO> getImages() throws Exception;
    /**
     * 搜索镜像
     * @param term
     * @param url
     * @return
     * @throws Exception
     */
    List<SearchItemVO> searchImages(String term) throws Exception;
    /**
     * 移除镜像
     * @param imageId
     * @param url
     * @throws Exception
     */
    void removeImage(String imageId) throws Exception;
    /**
     * 下载镜像
     * @param repository
     * @param url
     * @throws Exception
     */
    void pullImage(String repository) throws Exception;
}
