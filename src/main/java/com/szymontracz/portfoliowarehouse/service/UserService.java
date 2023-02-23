package com.szymontracz.portfoliowarehouse.service;

import com.szymontracz.portfoliowarehouse.security.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface UserService {

    ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file);

    byte[] downloadUserProfileImage(UUID userProfileId);

}
