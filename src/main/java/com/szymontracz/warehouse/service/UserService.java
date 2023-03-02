package com.szymontracz.warehouse.service;

import com.szymontracz.warehouse.auth.MessageResponse;
import com.szymontracz.warehouse.auth.Request;
import com.szymontracz.warehouse.dto.UserDto;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public interface UserService extends UserDetailsService {

    UserDto getUserById(UUID id);

//    UserDto saveNewUser(UserDto userDto);
//    Optional<UserDto> updateUserById(UUID uuid, UserDto userDto);
//    Boolean deleteUserById(UUID userId);

//    byte[] downloadUserProfileImage(UUID userProfileId);

    ResponseEntity<MessageResponse> registerNewUser(Request registertationRequest);

    ResponseCookie authenticate(Request loginRequest);

    ResponseCookie logoutUser();

    String confirm(String token);
}
