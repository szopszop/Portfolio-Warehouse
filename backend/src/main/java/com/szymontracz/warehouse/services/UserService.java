package com.szymontracz.warehouse.services;

import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.SignUpDto;
import com.szymontracz.warehouse.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public interface UserService {

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto userDto);

    UserDto findByLogin(String login);
}
