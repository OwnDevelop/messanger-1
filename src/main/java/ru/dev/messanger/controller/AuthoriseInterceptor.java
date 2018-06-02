package ru.dev.messanger.controller;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.dev.messanger.BLL.BLL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthoriseInterceptor implements HandlerInterceptor {
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
        String[] allowed = {"/authorization", "/loginAlreadyExists", "/signup", "/signin", "/css/", "/fonts/", "/img/", "/js/", "/psd/"};
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
