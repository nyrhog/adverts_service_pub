package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.AdvertRepository;
import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.dto.RegistrationDto;
import com.project.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
class AdminControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final AdvertRepository advertRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public AdminControllerTest(AdvertRepository advertRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.advertRepository = advertRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        User user = userRepository.findByUsername("nyrhog").orElse(null);

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "ADMIN")
    void enablePremStatus() throws Exception {

        AdvertPremium advertPremium = new AdvertPremium();

        Advert advert = new Advert();
        advert.setAdName("ad");
        advert.setAdPrice(123d);
        advert.setStatus(Status.ACTIVE);
        advert.setAdvertPremium(advertPremium);

        BillingDetails billingDetails = new BillingDetails();
        billingDetails.setDays(4);
        advert.setBillingDetails(billingDetails);

        advert = advertRepository.save(advert);
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/premium/" + advert.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        advert = advertRepository.getById(advert.getId());
        Assertions.assertTrue(advert.getAdvertPremium().getIsActive());

    }
}