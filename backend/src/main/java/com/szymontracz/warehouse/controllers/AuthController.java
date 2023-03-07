package com.szymontracz.warehouse.controllers;


import com.szymontracz.warehouse.config.UserAuthProvider;
import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
@RequestMapping
//@CrossOrigin(origins = {"http://localhost:5137"}, allowedHeaders = "*", allowCredentials = "true")
@CrossOrigin
public class AuthController {

  private final UserServiceImpl userService;
  private final UserAuthProvider userAuthProvider;

  public static final String loginPath = "/api/v1/auth/login";
  public static final String registerPath = "/api/v1/auth/register";
  public static final String logoutPath = "/api/v1/auth/logout";

  @PostMapping(loginPath)
  public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
    UserDto userDto = userService.login(credentialsDto);
    userDto.setToken(userAuthProvider.createToken(userDto.getEmail()));
    return ResponseEntity.ok(userDto);
  }

  @PostMapping(registerPath)
  public ResponseEntity<UserDto> register(@RequestBody @Valid CredentialsDto credentialsDto) {
    UserDto createdUser = userService.register(credentialsDto);
    createdUser.setToken(userAuthProvider.createToken(credentialsDto.getEmail()));
    return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
  }

  @PostMapping(logoutPath)
  public ResponseEntity<Void> logout(@AuthenticationPrincipal CredentialsDto credentialsDto) {
    SecurityContextHolder.clearContext();
    return ResponseEntity.noContent().build();
  }



}
