package ru.dev.messanger.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.dev.messanger.BLL.BLL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthoriseInterceptor implements HandlerInterceptor {

    private final BLL bll;

    public AuthoriseInterceptor(BLL bll) {
        this.bll = bll;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        if (isAllowed(url)){
            return true;
        } else if (bll.checkToken(request.getHeader("token"))){
            System.out.println(request.getHeader("token"));
            if (request.getHeader("token") == null)
            {
                return true;
            }
            return true;
        } else {
            return false;
        }
    }
    private Boolean isAllowed(String url){
        if (url.equals("/")) return true;
        String[] allowed = {"/enter", "/logout" ,"/signup", "/signin", "/css/", "/fonts/", "/img/", "/js/", "/psd/", "/activate/", "/setUser", "/loginAlreadyExists", "/authorization", "/main"};
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
