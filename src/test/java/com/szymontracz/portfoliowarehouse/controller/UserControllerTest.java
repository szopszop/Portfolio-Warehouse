package com.szymontracz.portfoliowarehouse.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Test
    void getUserById() {
    }

    @Test
    void saveNewUser() {
    }

    @Test
    void updateUserById() {
    }

    @Test
    void deleteBeerById() {
    }

    @Test
    void uploadUserProfileImage() {
    }

    @Test
    void downloadUserProfileImage() {
    }
}