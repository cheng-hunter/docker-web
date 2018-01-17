package net.sakz.docker.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.sakz.docker.Constants;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute(Constants.SESSION_KEY);
        if (obj == null) {
            response.sendRedirect(request.getServletContext().getContextPath());
            return false;
        }
        return true;
    }
}