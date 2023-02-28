package com.szymontracz.warehouse.controller;

import com.szymontracz.warehouse.model.UserDto;
import com.szymontracz.warehouse.model.web.CreateUserRequestModel;
import com.szymontracz.warehouse.model.web.UserResponseModel;
import com.szymontracz.warehouse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
//@CrossOrigin("http://localhost:5173/")
public class UserController {

    @Value("${token.secret}")
    String token;

    private final UserService userService;


    @Value("${server.port}")
    private String port;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel requestModel) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(requestModel, UserDto.class);

        UserDto createdUserDetails = userService.saveNewUser(userDto);

        UserResponseModel returnValue = modelMapper.map(createdUserDetails, UserResponseModel.class);
        log.info("User created with id: " + returnValue.getUserId().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

//    @PutMapping("/{userId}")
//    public ResponseEntity updateUserById(@PathVariable("userId") UUID userId, @Validated @RequestBody UserDto userDto) {
//        if (userService.updateUserById(userId, userDto).isEmpty()) {
//            throw new NotFoundException();
//        }
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity deleteBeerById(@PathVariable("userId") UUID userId) {
//        if (!userService.deleteUserById(userId)) {
//            throw new NotFoundException();
//        }
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }
//
//
//    @PostMapping(path = "{userProfileId}/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//                                                        produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<MessageResponse> uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
//                                                                  @RequestParam("imageFile") MultipartFile imageFile) {
//        return userService.uploadProfilePictureValidation(userProfileId, imageFile);
//    }
//
//    @GetMapping("{userProfileId}/image/download")
//    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
//        return userService.downloadUserProfileImage(userProfileId);
//    }
//

}
