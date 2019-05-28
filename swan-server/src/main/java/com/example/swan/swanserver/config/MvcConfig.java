package com.example.swan.swanserver.config;

import com.example.swan.swanserver.config.interceptors.KaptchaInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 配置类
 * @author entropyfeng
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Bean
    KaptchaInterceptor kaptchaInterceptor(){
        return new KaptchaInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(kaptchaInterceptor()).order(0);

    }
}
