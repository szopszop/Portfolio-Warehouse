package com.szymontracz.portfoliowarehouse.controller;

import com.szymontracz.portfoliowarehouse.security.MessageResponse;
import com.szymontracz.portfoliowarehouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("http://localhost:5173/")
public class UserController {

    private final UserService userService;


    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MessageResponse> uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                                                  @RequestParam("imageFile")MultipartFile imageFile) {
        return userService.uploadProfilePictureValidation(userProfileId, imageFile);
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return userService.downloadUserProfileImage(userProfileId);
    }

}
