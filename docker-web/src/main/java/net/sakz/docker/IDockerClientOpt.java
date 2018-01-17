package net.sakz.docker;

import com.spotify.docker.client.messages.Info;
import com.spotify.docker.client.messages.Version;

public interface IDockerClientOpt extends IDockerContainerOpt,IDockerImageOpt{
	/**
	 * 获取信息
	 * @param url
	 * @return
	 * @throws Exception
	 */
    Info getInfo() throws Exception;
    /**
     * 获取版本信息
     * @param url
     * @return
     * @throws Exception
     */
    Version getVersion() throws Exception;
    /**
     * 销毁对应的docker对象
     * @param serverUrl
     */
    public void destroyClient();
}
