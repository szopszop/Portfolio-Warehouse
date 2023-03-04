package com.szymontracz.warehouse.services;

import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.UserDto;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(CredentialsDto userDto);

    UserDto findByLogin(String login);
}
