package com.szymontracz.warehouse.dto;

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
