package com.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.AdvertRepository;
import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.dto.RegistrationDto;
import com.project.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = AdvertsServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class AdminControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private AdvertRepository advertRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public AdminControllerTest(AdvertRepository advertRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.advertRepository = advertRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @AfterEach
    private void resetDb() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "ADMIN")
    void registerAdmin() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("nyrhog");
        registrationDto.setPassword("test");
        registrationDto.setEmail("test@gmail.com");
        registrationDto.setFirstName("Kirill");
        registrationDto.setSurname("Kananovich");
        registrationDto.setPhoneNumber("335553535");

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        roleRepository.save(roleAdmin);

        mockMvc.perform(post("/admin/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isNoContent());

        User user = userRepository.findByUsername("nyrhog").orElse(null);

        assertNotNull(user);
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "ADMIN")
    void enablePremiumAdvert() throws Exception {

        AdvertPremium advertPremium = new AdvertPremium();
        advertPremium.setIsActive(false);

        Advert advert = new Advert();
        advert.setAdvertPremium(advertPremium);
        advert.setAdName("asd");
        advert.setAdPrice(123d);
        advert.setStatus(Status.ACTIVE);

        advertRepository.save(advert);

        mockMvc.perform(patch("/admin/premium?id=1&days=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        advert = advertRepository.findById(1L).orElse(null);

        assertNotNull(advert);
        assertTrue(advert.getAdvertPremium().getIsActive());
    }
}