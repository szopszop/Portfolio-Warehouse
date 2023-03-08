package com.szymontracz.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PortfolioWarehouseApp {
    
    public static void main(String[] args) {
        SpringApplication.run(PortfolioWarehouseApp.class, args);
    }


}
