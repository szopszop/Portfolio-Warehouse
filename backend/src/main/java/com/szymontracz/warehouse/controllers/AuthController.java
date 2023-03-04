package com.szymontracz.warehouse.controllers;


import com.szymontracz.warehouse.config.UserAuthProvider;
import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.SignUpDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.services.UserService;
import com.szymontracz.warehouse.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000/"}, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

  private final UserServiceImpl userService;
  private final UserAuthProvider userAuthProvider;

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
    UserDto userDto = userService.login(credentialsDto);
    userDto.setToken(userAuthProvider.createToken(userDto.getEmail()));
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
    UserDto createdUser = userService.register(user);
    createdUser.setToken(userAuthProvider.createToken(user.getEmail()));
    return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
  }



}
