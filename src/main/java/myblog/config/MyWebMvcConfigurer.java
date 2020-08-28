package myblog.config;

import lombok.extern.slf4j.Slf4j;
import myblog.interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FANG
 */
@Configuration
@Slf4j(topic = "MyConfig")
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("interceptors working");
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/coverImg/**").addResourceLocations("file:d:\\IdeaProjects\\MyBlog\\src\\main\\resources\\static\\admin\\coverImg\\");
    }
}
