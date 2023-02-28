package com.szymontracz.warehouse.service;

import com.szymontracz.warehouse.auth.AuthenticationRequest;
import com.szymontracz.warehouse.auth.AuthenticationResponse;
import com.szymontracz.warehouse.auth.RegisterRequest;
import com.szymontracz.warehouse.config.JwtService;
import com.szymontracz.warehouse.entity.Role;
import com.szymontracz.warehouse.entity.EmailToken;
import com.szymontracz.warehouse.entity.User;
import com.szymontracz.warehouse.repository.TokenRepository;
import com.szymontracz.warehouse.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

//    @Value("${confirmation.url}")
    private static String CONFIRMATION_URL="http://localhost:8080/api/v1/authentication/confirm?token=%s";

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
            private final JwtService jwtService;
            private final AuthenticationManager authenticationManager;

            @Transactional
            public AuthenticationResponse registerNewUser(RegisterRequest registrationDto) {
                if (isUserExists(registrationDto)) {
                    throw new IllegalStateException("Email already taken");
                }
                User newUser = createNewUser(registrationDto);
                userRepository.save(newUser);

                EmailToken newEmailToken = createNewToken(newUser);
                tokenRepository.save(newEmailToken);

                sendEmail(registrationDto, newEmailToken);

                var jwtToken = jwtService.generateToken(newUser);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }

            private boolean isUserExists(RegisterRequest registrationDto) {
                return userRepository.findByEmail(registrationDto.getEmail()).isPresent();
            }

            private User createNewUser(RegisterRequest registrationDto) {
                return User.builder()
                        .email(registrationDto.getEmail())
                        .password(passwordEncoder.encode(registrationDto.getPassword()))
                        .role(Role.USER)
                        .build();
            }

            private EmailToken createNewToken(User newUser) {
                return EmailToken.builder()
                        .token(UUID.randomUUID().toString())
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(10))
                        .user(newUser)
                        .build();
            }

            private void sendEmail(RegisterRequest registrationDto, EmailToken newEmailToken) {
                try {
                    emailService.send(
                        registrationDto.getEmail(),
                        null,
                        String.format(CONFIRMATION_URL, newEmailToken.getToken())
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
