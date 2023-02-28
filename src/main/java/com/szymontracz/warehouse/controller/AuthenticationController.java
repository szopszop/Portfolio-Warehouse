package com.szymontracz.warehouse.controller;

import com.szymontracz.warehouse.auth.AuthenticationRequest;
import com.szymontracz.warehouse.auth.AuthenticationResponse;
import com.szymontracz.warehouse.service.RegistrationService;
import com.szymontracz.warehouse.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final RegistrationService registrationService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(registrationService.registerNewUser(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(registrationService.authenticate(request));
  }


}
