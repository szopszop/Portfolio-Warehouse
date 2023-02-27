package com.szymontracz.warehouse.service;

import com.szymontracz.warehouse.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserDto getUserById(UUID id);
    UserDto getUserByEmail(String email);
    UserDto getUserByUsername(String email);
    UserDto saveNewUser(UserDto userDto);
//    Optional<UserDto> updateUserById(UUID uuid, UserDto userDto);
//    Boolean deleteUserById(UUID userId);

//    byte[] downloadUserProfileImage(UUID userProfileId);
}
