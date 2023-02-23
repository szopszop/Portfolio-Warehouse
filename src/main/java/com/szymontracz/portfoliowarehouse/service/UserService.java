package com.szymontracz.portfoliowarehouse.service;

import com.szymontracz.portfoliowarehouse.model.UserDto;
import com.szymontracz.portfoliowarehouse.security.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {

    ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file);

    byte[] downloadUserProfileImage(UUID userProfileId);

    Optional<UserDto> getUserById(UUID id);
    UserDto saveNewUser(UserDto userDto);
    Optional<UserDto> updateUserById(UUID uuid, UserDto userDto);
    Boolean deleteUserById(UUID userId);

}
