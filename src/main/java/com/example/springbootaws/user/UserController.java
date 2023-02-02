package com.example.springbootaws.user;

import com.example.springbootaws.security.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("http://localhost:5173/")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

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
