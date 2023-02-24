package com.szymontracz.model;

import com.szymontracz.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Builder
@Data
@ToString
public class UserDto {

    private UUID id;

    @NotBlank
    private String username;
    @NotBlank
    private String email;
    private String userProfileImageLink;
    private String password;
    private User.Role role;




}
