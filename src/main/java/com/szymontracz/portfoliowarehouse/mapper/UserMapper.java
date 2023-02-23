package com.szymontracz.portfoliowarehouse.mapper;

import com.szymontracz.portfoliowarehouse.entity.User;
import com.szymontracz.portfoliowarehouse.model.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User userDtoToUser(UserDto dto);
    UserDto userToUserDto(User user);
}
