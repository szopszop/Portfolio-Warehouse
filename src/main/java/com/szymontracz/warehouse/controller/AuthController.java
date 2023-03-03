package com.szymontracz.warehouse.controller;

import com.szymontracz.warehouse.auth.AuthResponse;
import com.szymontracz.warehouse.auth.AuthRequest;
import com.szymontracz.warehouse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000/"}, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequestDto) throws IOException {
    return userService.registerNewUser(authRequestDto);
  }
  @PostMapping("/authenticate")
  public ResponseEntity<ResponseCookie> authenticate(@RequestBody AuthRequest authRequestDto) {
    ResponseCookie jwtCookie = userService.authenticate(authRequestDto);
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(jwtCookie);
  }
  @GetMapping("/current")
  public Object currentUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    return authentication.getPrincipal();
  }
  @PostMapping("/logout")
  public ResponseEntity<String> logoutUser() {
    ResponseCookie cookie = userService.logoutUser();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body("Signed out");
  }
  @GetMapping("/confirm")
  public ResponseEntity<String> confirm(@RequestParam String token) {
    return ResponseEntity.ok(userService.confirm(token));
  }


}
