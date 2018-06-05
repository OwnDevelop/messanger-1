package ru.dev.messanger.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.dev.messanger.BLL.BLL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthoriseInterceptor implements HandlerInterceptor {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (isAllowed(url)){
            return true;
        } else if (BLL.INSTANCE.checkToken(request.getHeader("token"))){
            System.out.println(request.getHeader("token"));
            return true;
        } else {
            return false;
        }
    }

    private Boolean isAllowed(String url){
        if (url.equals("/")) return true;
        String[] allowed = {"/logout" ,"/signup", "/signin", "/css/", "/fonts/", "/img/", "/js/", "/psd/", "/activate/", "/setUser"};
        for (String str: allowed) {
            if (url.indexOf(str) == 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
