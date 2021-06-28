package com.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.UserRepository;
import com.project.dto.AuthenticationRequestDto;
import com.project.dto.RegistrationDto;
import com.project.dto.RestorePasswordDto;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = AdvertsServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService userService;

    @BeforeAll
    @Rollback(value = false)
    void init(){
        User user = new User();

        Profile profile = new Profile()
                .setName("Kirill")
                .setPhoneNumber("123123123")
                .setSurname("Kananovich");

        user.setPassword("test")
                .setUsername("nyrhog")
                .setEmail("xxxxxxxxx@gmail.ru")
                .setProfile(profile);

        userService.register(user);
    }

    @Test
    void login() throws Exception {

        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
        authenticationRequestDto.setUsername("nyrhog");
        authenticationRequestDto.setPassword("test");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nyrhog"));

    }

    @Test
    void registration() throws Exception {

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("nyrhog2");
        registrationDto.setPassword("test");
        registrationDto.setEmail("test22@gmail.com");
        registrationDto.setFirstName("Kirill");
        registrationDto.setSurname("Kananovich");
        registrationDto.setPhoneNumber("335553535");

        mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());

        List<User> all = userRepository.findAll();
        assertEquals("nyrhog", all.get(0).getUsername());
    }

    @Test
    void sendMessage() throws Exception {

        mockMvc.perform(put("/auth/restore-password?username=nyrhog")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        User user = userRepository.findByUsername("nyrhog").orElse(null);

        assertNotNull(user);
        assertNotNull(user.getGeneratedValue());
    }

    @Test
    void restorePassword() throws Exception {
        userService.sendMessageWithCode("nyrhog");
        User user = userRepository.findByUsername("nyrhog").orElse(null);
        Integer generatedValue = user.getGeneratedValue();

        RestorePasswordDto restorePasswordDto = new RestorePasswordDto();
        restorePasswordDto.setCode(generatedValue);
        restorePasswordDto.setUsername(user.getUsername());
        restorePasswordDto.setNewPassword("asdasdasd");

        mockMvc.perform(patch("/auth/restore-password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restorePasswordDto)))
                .andExpect(status().isNoContent());

        user = userRepository.findByUsername("nyrhog").orElse(null);

        assertNull(user.getGeneratedValue());
    }


}