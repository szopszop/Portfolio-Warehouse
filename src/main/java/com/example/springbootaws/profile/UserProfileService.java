package com.example.springbootaws.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService) {
        this.userProfileDataAccessService = userProfileDataAccessService;
    }


    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }


    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. check if image is not empty
        // 2. check if file is an image
        // 3. check if user exists in database
        // 4. grab some medata from file if any
        // 5. store the image in s3 and update database with s3 image & link

    }
}
