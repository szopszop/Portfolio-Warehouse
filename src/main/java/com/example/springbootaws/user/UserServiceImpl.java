package com.example.springbootaws.user;

import com.example.springbootaws.amazon.bucket.BucketName;
import com.example.springbootaws.amazon.filestore.FileStore;
import com.example.springbootaws.security.auth.MessageResponse;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileStore fileStore;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FileStore fileStore) {
        this.userRepository = userRepository;
        this.fileStore = fileStore;
    }

    @Override
    public List<User> getAllUserProfiles() {
        return userRepository.findAllByUsernameExists();
    }

    @Override
    public ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file) {
        if (isFileNotEmpty(file) && isFileAnImage(file) &&
                userRepository.findUserByUserProfileId(userProfileId).isPresent()) {
            User user = userRepository.findUserByUserProfileId(userProfileId).get();
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
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
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
        if (userRepository.findUserByUserProfileId(userProfileId).isPresent()) {
            User user = userRepository.findUserByUserProfileId(userProfileId).get();
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
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
}
