package com.cpu.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:3000", "https://www.cpujbnu.co.kr", "https://cpujbnu.co.kr");
    }
}
