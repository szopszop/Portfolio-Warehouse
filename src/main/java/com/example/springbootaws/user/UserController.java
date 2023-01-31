package com.example.springbootaws.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface UserController {

    List<User> getUserProfiles();
    void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                       @RequestParam("imageFile") MultipartFile imageFile);
    byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId);

}
