package com.szymontracz.warehouse.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.szymontracz.warehouse.dtos.CredentialsDto;
import com.szymontracz.warehouse.entities.User;
import com.szymontracz.warehouse.repositories.UserRepository;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.szymontracz.warehouse.controllers.AuthController.loginPath;
import static com.szymontracz.warehouse.controllers.AuthController.registerPath;
import static com.szymontracz.warehouse.services.UserServiceImpl.emailAlreadyExistsMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class AuthControllerIT {

    private MockMvc mockMvc;

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
                .email("email@email.com")
                .password("password")
                .build();
        userRepository.save(user);

        CredentialsDto credentialsDto = CredentialsDto.builder()
                .email("email@email.com")
                .password("password".toCharArray())
                .build();

        MvcResult result = mockMvc.perform(post(registerPath)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(credentialsDto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        assertEquals(emailAlreadyExistsMessage, json.getString("message"));
    }
    @Test
    public void testLoginWithNonExistingEmailThrowsException() throws Exception {
        CredentialsDto credentialsDto = CredentialsDto.builder()
                .email("nonExitingEmail@email.com")
                .password("password".toCharArray())
                .build();

        MvcResult result = mockMvc.perform(post(loginPath)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(credentialsDto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        assertEquals(emailAlreadyExistsMessage, json.getString("message"));
    }



}
