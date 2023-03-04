package com.szymontracz.warehouse.dtos;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    @Email
    private String email;
    private String token;
}
