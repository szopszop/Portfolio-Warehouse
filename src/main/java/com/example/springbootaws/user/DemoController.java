package com.example.springbootaws.user;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class DemoController {

    @GetMapping("/")
    public String publicEntryPoint() {
        return "<h1> Hello </h1>";
    }
}

