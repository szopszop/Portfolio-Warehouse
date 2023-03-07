package com.szymontracz.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class PortfolioWarehouseApp {
    
    public static void main(String[] args) {
        SpringApplication.run(PortfolioWarehouseApp.class, args);
    }


}
