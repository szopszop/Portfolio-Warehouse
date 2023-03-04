package com.szymontracz.warehouse.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.dtos.UserDto;
import com.szymontracz.warehouse.exceptions.ResourceNotFoundException;
import com.szymontracz.warehouse.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static com.szymontracz.warehouse.controllers.AuthController.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class AuthControllerTest {

    public MockMvc mockMvc;
    private static PodamFactory podamFactory;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthController authController;

    @MockBean
    public UserServiceImpl userService;


    @BeforeAll
    public static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegister() throws Exception {
        // given
        CredentialsDto credentialsDto = podamFactory.manufacturePojo(CredentialsDto.class);
        UserDto userDto = podamFactory.manufacturePojo(UserDto.class);

        when(userService.register(any())).thenReturn(userDto);

        // when then
        mockMvc.perform(post(registerPath)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(credentialsDto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.token", is(userDto.getToken())));
    }

    @Test
    void testLoginWithBadCredentialsThrowsException() throws Exception {
        // given
        CredentialsDto credentialsDto = CredentialsDto.builder()
                .email("email")
                .password("password".toCharArray())
                .build();

        when(userService.login(credentialsDto)).thenThrow(ResourceNotFoundException.class);
        // when then
        mockMvc.perform(post(loginPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(credentialsDto)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }



    @Test
    void testLogout() throws Exception{
        //given when then
        mockMvc.perform(post(logoutPath))
                .andExpect(status().is(204));
    }




}