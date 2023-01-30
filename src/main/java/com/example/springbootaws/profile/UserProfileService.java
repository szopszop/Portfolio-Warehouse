package com.example.springbootaws.profile;

import com.example.springbootaws.bucket.BucketName;
import com.example.springbootaws.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.util.MimeTypeUtils.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        if (isFileNotEmpty(file) && isFileAnImage(file) && isUserInDatabase(userProfileId)) {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfileId);
            String filename = String.format("%s-%s", file.getName(), UUID.randomUUID());
            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private boolean isFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("File is empty!");
        } else {
            return true;
        }
    }

    private boolean isFileAnImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image!");
        } else {
            return true;
        }
    }

    private boolean isUserInDatabase(UUID userProfileId) {
        if (userProfileDataAccessService.getSingleUserProfile(userProfileId).isEmpty()) {
            throw new IllegalStateException("User not found!");
        } else {
            return true;
        }
    }

}
