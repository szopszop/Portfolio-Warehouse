package com.szymontracz.controller;

import com.szymontracz.model.UserDto;
import com.szymontracz.security.MessageResponse;
import com.szymontracz.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/user")
//@CrossOrigin("http://localhost:5173/")
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDto getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity saveNewUser(@Validated @RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveNewUser(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/user/" + savedUser.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateUserById(@PathVariable("userId") UUID userId, @Validated @RequestBody UserDto userDto) {
        if (userService.updateUserById(userId, userDto).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity deleteBeerById(@PathVariable("userId") UUID userId) {
        if (!userService.deleteUserById(userId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping(path = "{userProfileId}/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                                                        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                                                  @RequestParam("imageFile")MultipartFile imageFile) {
        return userService.uploadProfilePictureValidation(userProfileId, imageFile);
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return userService.downloadUserProfileImage(userProfileId);
    }


}
