package com.yikekong.httpfilter;

import com.google.common.base.Strings;
import com.yikekong.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*",filterName = "authFilter")
public class AuthFilter implements Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        //登录不校验token
        if(path.equals("/login")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //tag接口不校验token tag接口供外部系统调用
        if(path.contains("/device/tags")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //设备断链监控，不需要登陆 放行
        if(path.equals("/device/clientAction")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if(Strings.isNullOrEmpty(authToken)){
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        try {
            JwtUtil.parseJWT(authToken);
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


}
