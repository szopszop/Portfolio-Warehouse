package com.szymontracz.warehouse.mappers;

import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    void testUserMapper() {
        // given
        User user = User.builder()
                .id(1L)
                .email("email")
                .password("password")
                .build();

        // when
        UserDto userDto = mapper.toUserDto(user);

        // then
        assertAll(
                () -> {
                    assertEquals(user.getEmail(), userDto.getEmail());
                    assertEquals(user.getId(), userDto.getId());
                }
        );
    }
}