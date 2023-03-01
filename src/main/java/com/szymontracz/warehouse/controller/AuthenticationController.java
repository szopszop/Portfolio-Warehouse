package com.szymontracz.warehouse.controller;

import com.szymontracz.warehouse.auth.AuthenticationRequest;
import com.szymontracz.warehouse.auth.AuthenticationResponse;
import com.szymontracz.warehouse.service.RegistrationService;
import com.szymontracz.warehouse.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final RegistrationService registrationService;

  @PostMapping("/register")
  public HttpServletResponse register(@RequestBody RegisterRequest request, HttpServletResponse httpServletResponse) throws IOException {
    AuthenticationResponse authenticationResponse = registrationService.registerNewUser(request);
    httpServletResponse.setHeader("Set-Cookie", "Warehouse=" + authenticationResponse.getToken() + "; Path=/; Max-Age=604800; HttpOnly; SameSite=None");
    httpServletResponse.setStatus(HttpStatus.OK.value());
    return httpServletResponse;
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletRequest httpServletRequest) {
    return ResponseEntity.ok(registrationService.authenticate(request));
  }

}
