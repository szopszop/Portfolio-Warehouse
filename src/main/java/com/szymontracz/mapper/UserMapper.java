package com.szymontracz.mapper;

import com.szymontracz.entity.User;
import com.szymontracz.model.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User userDtoToUser(UserDto dto);
    UserDto userToUserDto(User user);
}
