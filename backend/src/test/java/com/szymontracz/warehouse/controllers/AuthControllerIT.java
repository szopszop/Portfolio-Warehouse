package com.szymontracz.warehouse.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.entities.User;
import com.szymontracz.warehouse.exceptions.BadArgumentsException;
import com.szymontracz.warehouse.exceptions.ResourceNotFoundException;
import com.szymontracz.warehouse.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.szymontracz.warehouse.controllers.AuthController.loginPath;
import static com.szymontracz.warehouse.controllers.AuthController.registerPath;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class AuthControllerIT {

    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testRegisterWithExistingEmailThrowsException() throws Exception {
        User user = User.builder()
                .email("email")
                .password("password")
                .build();
        userRepository.save(user);

        CredentialsDto credentialsDto = CredentialsDto.builder()
                .email("email")
                .password("password".toCharArray())
                .build();

        mockMvc.perform(post(registerPath)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(credentialsDto)))
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentsException))
                .andExpect(result -> Assertions.assertEquals("Login already exists", result.getResolvedException().getMessage()));

    }



}
