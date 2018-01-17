package net.sakz.docker.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import net.sakz.docker.Constants;
import net.sakz.docker.DockerClientManger;
import net.sakz.docker.bean.ContainerVO;
import net.sakz.docker.bean.FieldUtil;
import net.sakz.docker.bean.ImageVO;
import net.sakz.docker.bean.Response;
import net.sakz.docker.bean.SearchItemVO;

@Controller
@RequestMapping("/mgr")
public class DockerMgrController {

  

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(Model model,HttpSession session) throws Exception {
        model.addAttribute("info", JSON.toJSONString(DockerClientManger.getInstance()
        		.getOnlyClientByUrl((session.getAttribute(Constants.SESSION_KEY).toString())).getInfo()));
        return "mgr/info";
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String version(Model model,HttpSession session) throws Exception {
        model.addAttribute("version", JSON.toJSONString(DockerClientManger.getInstance()
        		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).getVersion()));
        return "mgr/version";
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public String images(Model model,HttpSession session) throws Exception {
        model.addAttribute("imageVOs", DockerClientManger.getInstance()
        		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).getImages());
        model.addAttribute("headers", FieldUtil.getHeader(ImageVO.class));
        return "mgr/images";
    }

    @RequestMapping(value = "/image/search", method = RequestMethod.GET)
    public String searchImages(@RequestParam("term") String term, Model model,HttpSession session) throws Exception {
    	if (StringUtils.isNotEmpty(term)) {
    	model.addAttribute("items", DockerClientManger.getInstance()
        		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).searchImages(term));
        model.addAttribute("headers", FieldUtil.getHeader(SearchItemVO.class));
    	}
        return "mgr/search_images";
    }
    
    @RequestMapping(value = "/go/container/{cid}", method = RequestMethod.GET)
    public String goContainer(@PathVariable("cid") String id, Model model,HttpSession session) {
    	if (StringUtils.isNotEmpty(id)) {
    	  model.addAttribute("ip", "127.0.0.1");
    	  model.addAttribute("port", "8080");
    	  model.addAttribute("id", id);
    	}
        return "mgr/go_containers";
    }
    /**
     * 显示所有正在运行的容器
     * @param term
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/containers", method = RequestMethod.GET)
    public String containers(Model model,HttpSession session) throws Exception {
        model.addAttribute("items", DockerClientManger.getInstance()
        		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).getContainer());
        model.addAttribute("headers", FieldUtil.getHeader(ContainerVO.class));
        return "mgr/containers";
    }

    @ResponseBody
    @RequestMapping(value = "/image/remove/{imageId}", method = RequestMethod.POST)
    public Response<String> removeImage(@PathVariable("imageId") String imageId,HttpSession session) {
        Response<String> response;
        try {
        	DockerClientManger.getInstance()
    		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).removeImage(imageId);
            response = new Response<>(true, "");
        } catch (Exception e) {
            response = new Response<>(false, "");
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/image/pull/{repository}", method = RequestMethod.POST)
    public Response<String> pullImage(@PathVariable("repository") String repository,HttpSession session) {
        Response<String> response;
        try {
        	DockerClientManger.getInstance()
    		.getOnlyClientByUrl(session.getAttribute(Constants.SESSION_KEY).toString()).pullImage(repository);
            response = new Response<>(true, "");
        } catch (Exception e) {
            response = new Response<>(false, "");
        }
        return response;
    }
}