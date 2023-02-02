package com.example.springbootaws.security.oauth;


import com.example.springbootaws.security.payload.response.MessageResponse;
import com.example.springbootaws.security.payload.request.LoginRequest;
import com.example.springbootaws.security.payload.request.RegisterRequest;
import com.example.springbootaws.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000/"}, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

    private final UserService userService;


    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> addUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.addUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseCookie> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseCookie jwtCookie = userService.authenticateUser(loginRequest);
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
                .body("You've been signed out!");
    }

}
