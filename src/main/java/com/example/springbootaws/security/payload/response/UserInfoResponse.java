package com.example.springbootaws.security.payload.response;

import com.example.springbootaws.user.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoResponse {
    private UUID userID;
    private String email;
    private User.Role role;


    public UserInfoResponse(UUID userID, String email, User.Role role) {
        this.userID = userID;
        this.email = email;
        this.role = role;
    }
}
