package com.example.springbootaws.profile;

import com.example.springbootaws.bucket.BucketName;
import com.example.springbootaws.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getAllUserProfiles() {
        return userProfileDataAccessService.getAllUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        if (isFileNotEmpty(file) && isFileAnImage(file) && isUserInDatabase(userProfileId)) {
            Map<String, String> metadata = extractMetadata(file);
            UserProfile user = userProfileDataAccessService.getUserProfile(userProfileId);
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
            String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
                user.setUserProfileImageLink(filename);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private boolean isFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("File is empty! [ " + file.getSize() + " ]");
        } else {
            return true;
        }
    }

    private boolean isFileAnImage(MultipartFile file) {
        if(!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image! [ " + file.getContentType() + " ]");
        } else {
            return true;
        }
    }

    private boolean isUserInDatabase(UUID userProfileId) {
        if (userProfileDataAccessService.getUserProfile(userProfileId).getClass() != UserProfile.class) {
            throw new IllegalStateException("User not found!");
        } else {
            return true;
        }
    }

    private UserProfile getUserProfile(UUID userProfileId) {
        return userProfileDataAccessService.getUserProfile(userProfileId);
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfile(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());

        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }
}
