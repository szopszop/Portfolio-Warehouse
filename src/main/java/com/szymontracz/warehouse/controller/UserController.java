package com.szymontracz.warehouse.controller;

import com.szymontracz.warehouse.dto.UserDto;

import com.szymontracz.warehouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
//@CrossOrigin("http://localhost:5173/")
public class UserController {

    @Value("${token.secret}")
    String token;

    private final UserService userService;


    @Value("${server.port}")
    private String port;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId);
    }





}
