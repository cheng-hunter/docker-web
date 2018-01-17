package net.sakz.docker.web.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import net.sakz.docker.Constants;

/**
 * websocket拦截器.
 * <p>主要用于获得传参，ip,containerId,width,height</p>
 * @author will
 */
public class ContainerExecHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
        }
        String serverUrl=((ServletServerHttpRequest) request)
        		.getServletRequest().getSession().getAttribute(Constants.SESSION_KEY).toString();
        String containerId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("containerId");
        String width = ((ServletServerHttpRequest) request).getServletRequest().getParameter("width");
        String height = ((ServletServerHttpRequest) request).getServletRequest().getParameter("height");
        attributes.put("serverUrl",serverUrl);
        attributes.put("containerId",containerId);
        attributes.put("width",width);
        attributes.put("height",height);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}