package com.szymontracz.warehouse.services;

import com.szymontracz.warehouse.entities.User;
import com.szymontracz.warehouse.mappers.UserMapper;
import com.szymontracz.warehouse.mappers.UserMapperImpl;
import com.szymontracz.warehouse.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Test
    void  loginMethodReturnsExistingUserDto() {
        // given
        Optional<User> user = Optional.of(User.builder()
                .id(1L)
                .email("email")
                .password("password")
                .build()
        );

        // when


    }
}