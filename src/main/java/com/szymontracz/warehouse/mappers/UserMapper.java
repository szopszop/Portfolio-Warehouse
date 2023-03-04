package com.szymontracz.warehouse.mappers;

import com.szymontracz.warehouse.dtos.SignUpDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
