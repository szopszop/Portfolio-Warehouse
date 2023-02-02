package com.example.springbootaws.user;

import com.example.springbootaws.amazon.bucket.BucketName;
import com.example.springbootaws.amazon.filestore.FileStore;
import com.example.springbootaws.security.payload.response.MessageResponse;
import com.example.springbootaws.security.jwt.JwtUtils;
import com.example.springbootaws.security.payload.request.LoginRequest;
import com.example.springbootaws.security.payload.request.RegisterRequest;
import com.example.springbootaws.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileStore fileStore;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, FileStore fileStore, PasswordEncoder encoder,
                           AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.fileStore = fileStore;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @Override
    @Transactional
    public ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file) {
        if (isFileNotEmpty(file) && isFileAnImage(file) &&
                userRepository.findUserByUserId(userProfileId).isPresent()) {
            User user = userRepository.findUserByUserId(userProfileId).get();
            Map<String, String> metadata = extractMetadata(file);
            uploadProfilePicture(user, file, metadata);
            return ResponseEntity.ok(new MessageResponse("Image uploaded successfully."));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("There's been an error, please try again."));
    }

    @Override
    public ResponseEntity<MessageResponse> addUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email already in use."));
        }
        userRepository.save(new User(registerRequest.getEmail(), encoder.encode(registerRequest.getPassword())));
        return ResponseEntity.ok(new MessageResponse("User registered successfully."));
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void uploadProfilePicture(User user, MultipartFile file, Map<String, String> metadata) {
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] downloadUserProfileImage(UUID userProfileId) {
        if (userRepository.findUserByUserId(userProfileId).isPresent()) {
            User user = userRepository.findUserByUserId(userProfileId).get();
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserId());
            return user.getUserProfileImageLink().map(key -> fileStore.download(path, key)).orElse(new byte[0]);
        }
        return new byte[0];
    }

    private boolean isFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("File is empty! [ " + file.getSize() + " ]");
        } else {
            return true;
        }
    }

    private boolean isFileAnImage(MultipartFile file) {
        if (!Arrays.asList(
                        ContentType.IMAGE_JPEG.getMimeType(),
                        ContentType.IMAGE_PNG.getMimeType(),
                        ContentType.IMAGE_GIF.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image! [ " + file.getContentType() + " ]");
        } else {
            return true;
        }
    }


    @Override
    public ResponseCookie authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwtUtils.generateJwtCookie(userDetails.getEmail());
    }

    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}
