package com.szymontracz.warehouse.services;

import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.entities.User;
import com.szymontracz.warehouse.exceptions.AppException;
import com.szymontracz.warehouse.mappers.UserMapper;
import com.szymontracz.warehouse.repositories.TokenRepository;
import com.szymontracz.warehouse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Value("${confirmation.url}")
    private String CONFIRMATION_URL;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    public final static String emailAlreadyExistsMessage = "Email already exists";
    public final static String unknownUserMessage = "Unknown user";
    public final static String invalidCredentials = "Invalid credentials";


    @Override
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException(unknownUserMessage, HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException(invalidCredentials, HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDto register(CredentialsDto credentialsDto) {
        Optional<User> optionalUser = userRepository.findByEmail(credentialsDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException(emailAlreadyExistsMessage, HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(credentialsDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(credentialsDto.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByEmail(login)
                .orElseThrow(() -> new AppException(unknownUserMessage, HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

}