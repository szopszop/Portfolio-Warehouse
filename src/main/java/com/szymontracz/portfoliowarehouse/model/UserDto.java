package com.szymontracz.portfoliowarehouse.model;

import com.szymontracz.portfoliowarehouse.entity.User;
import java.util.UUID;

public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private String userProfileImageLink;
    private String password;
    private User.Role role;




}
