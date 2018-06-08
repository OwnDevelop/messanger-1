package ru.dev.messanger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dev.messanger.BLL.BLL;
import ru.dev.messanger.controller.AuthoriseInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public WebMvcConfig(BLL bll) {
        this.bll = bll;
    }

    private final BLL bll;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${image.profile.path}")
    private String uploadProfilePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthoriseInterceptor(bll));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/uploads/**")
                .addResourceLocations("file://" + uploadPath + "/");
        registry.addResourceHandler("/img/profiles/**")
                .addResourceLocations("file://" + uploadProfilePath + "/");
        registry.addResourceHandler("/static/**") //TODO: ВРОДЕ КАК НЕ ОБЯЗАТЕЛЬНО
                .addResourceLocations("classpath:/static/");   //
    }
}