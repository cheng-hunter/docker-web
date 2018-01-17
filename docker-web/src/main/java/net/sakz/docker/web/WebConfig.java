package net.sakz.docker.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import net.sakz.docker.web.handler.ContainerExecWSHandler;
import net.sakz.docker.web.interceptor.ContainerExecHandshakeInterceptor;
import net.sakz.docker.web.interceptor.SessionInterceptor;

@Configuration
@EnableWebSocket
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/mgr/**");
    }
    
    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }

    @Bean
    public ContainerExecWSHandler containerExecWSHandler(){
        return new ContainerExecWSHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(containerExecWSHandler(), "/ws/container/exec").addInterceptors(new ContainerExecHandshakeInterceptor());
    }
}