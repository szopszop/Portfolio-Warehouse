package com.example.springbootaws.user;

import com.example.springbootaws.security.payload.response.MessageResponse;
import com.example.springbootaws.security.payload.request.LoginRequest;
import com.example.springbootaws.security.payload.request.RegisterRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface UserService {

    ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file);
    ResponseEntity<MessageResponse> addUser(RegisterRequest registerRequest);
    byte[] downloadUserProfileImage(UUID userProfileId);
    ResponseCookie authenticateUser(LoginRequest loginRequest);
    ResponseCookie logoutUser();

    boolean existsByEmail(String email);

}
