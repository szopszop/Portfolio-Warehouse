package com.szymontracz.portfoliowarehouse.service;


import com.szymontracz.portfoliowarehouse.amazon.bucket.BucketName;
import com.szymontracz.portfoliowarehouse.amazon.filestore.FileStore;
import com.szymontracz.portfoliowarehouse.entity.User;
import com.szymontracz.portfoliowarehouse.mapper.UserMapper;
import com.szymontracz.portfoliowarehouse.model.UserDto;
import com.szymontracz.portfoliowarehouse.repository.UserRepository;
import com.szymontracz.portfoliowarehouse.security.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FileStore fileStore;
    private final UserRepository userRepository;

    private final UserMapper userMapper;



    @Override
    public ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file) {
        if (isFileNotEmpty(file) && isFileAnImage(file) &&
                userRepository.findUserById(userProfileId).isPresent()) {
            User user = userRepository.findUserById(userProfileId).get();
            Map<String, String> metadata = extractMetadata(file);
            uploadProfilePicture(user, file, metadata);
            return ResponseEntity.ok(new MessageResponse("Image uploaded successfully."));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("There's been an error, please try again."));
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void uploadProfilePicture(User user, MultipartFile file, Map<String, String> metadata) {
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
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
        if (userRepository.findUserById(userProfileId).isPresent()) {
            User user = userRepository.findUserById(userProfileId).get();
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
            return user.getUserProfileImageLink().map(key -> fileStore.download(path, key)).orElse(new byte[0]);
        }
        return new byte[0];
    }

    @Override
    public Optional<UserDto> getUserById(UUID id) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findUserById(id).orElse(null)));
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findByEmail(email).orElse(null)));
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findByUsername(username).orElse(null)));
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {
        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
    }

    @Override
    public Optional<UserDto> updateUserById(UUID uuid, UserDto userDto) {
        return Optional.empty();
    }

    @Override
    public Boolean deleteUserById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (userRepository.findByUsername(username).isPresent()) {
            user = userRepository.findByUsername(username).get();
        } else {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getId(), user.getUsername(), user.getEmail(),
                Optional.of(user.getUserProfileImageLink().get()).orElse(null),
                user.getPassword(), user.getRole());
    }
}
