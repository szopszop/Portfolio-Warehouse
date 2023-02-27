package com.szymontracz.warehouse.model;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
public class UserDto {

    private UUID userId;
    private String username;
    private String email;
    private String password;
    private String encryptedPassword;
}
