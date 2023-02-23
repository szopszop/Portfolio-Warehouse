package com.example.springbootaws.model;

import com.example.springbootaws.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private String userProfileImageLink;
    private String password;
    private User.Role role;




}
