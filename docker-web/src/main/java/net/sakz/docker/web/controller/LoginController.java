package net.sakz.docker.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sakz.docker.Constants;
import net.sakz.docker.DockerClientManger;
import net.sakz.docker.bean.Response;
import net.sakz.docker.web.service.RedisService;

@Controller
public class LoginController {
	 @Autowired
	 private RedisService redisService;
	 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @ResponseBody
    @RequestMapping(value = "connect", method = RequestMethod.POST)
    public Response<String> connect(@RequestParam("serverUrl") String serverUrl, HttpSession session) {
        Response<String> response;
        try {
            response = new Response<>(true, "");
            session.setAttribute(Constants.SESSION_KEY, serverUrl);
            DockerClientManger.getInstance().setRedisService(redisService);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(false, "");
        }
        return response;
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) throws Exception {
    	DockerClientManger.getInstance().destroyClient(session.getAttribute(Constants.SESSION_KEY).toString());
        session.removeAttribute(Constants.SESSION_KEY);
        return "redirect:/login";
    }
}