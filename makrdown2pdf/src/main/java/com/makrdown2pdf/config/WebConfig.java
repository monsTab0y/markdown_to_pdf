package com.makrdown2pdf.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allows CORS on all endpoints
                .allowedOrigins("http://localhost:3000") // Allow frontend to make requests
                .allowedOrigins("https://cool-florentine-b24eb2.netlify.app")
                .allowedOrigins("https://papertailor.org")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authentication)
    }
}
