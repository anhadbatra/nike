package com.nike.nike;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the URL "/uploaded-images/**" to the physical directory
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:D:/Developing_Web_Applications/nike/src/main/resources/uploaded-images/");
    }
}
