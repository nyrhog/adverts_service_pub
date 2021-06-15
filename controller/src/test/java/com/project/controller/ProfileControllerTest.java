package com.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.*;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.dto.RegistrationDto;
import com.project.entity.Category;
import com.project.entity.Profile;
import com.project.entity.Rating;
import com.project.entity.User;
import com.project.service.IUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final ProfileRepository profileRepository;
    private final IUserService userService;
    private final RatingRepository ratingRepository;

    @Autowired
    public ProfileControllerTest(ProfileRepository profileRepository, IUserService userService, RatingRepository ratingRepository) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.ratingRepository = ratingRepository;
    }

    @BeforeAll
    private void initBefore() {
        RegistrationDto requestDto = new RegistrationDto();
        requestDto.setFirstName("asd");
        requestDto.setSurname("asd");
        requestDto.setPassword("asd");
        requestDto.setEmail("xxx@mail.ru");
        requestDto.setUsername("nyrhog");
        requestDto.setPhoneNumber("123123123");

        User user = new User();

        Profile profile = new Profile()
                .setName(requestDto.getFirstName())
                .setPhoneNumber(requestDto.getPhoneNumber())
                .setSurname(requestDto.getSurname());

        user.setPassword(requestDto.getPassword())
                .setUsername(requestDto.getUsername())
                .setEmail(requestDto.getEmail())
                .setProfile(profile);

        userService.register(user);
    }



    @Test
    @WithMockUser(username = "nyrhog")
    void updateProfile() throws Exception {

        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setName("NewName");
        updateDto.setId(1L);

        Profile profile = profileRepository.findById(1L).orElse(null);
        String oldName = profile.getName();
        assertEquals("asd", oldName);
        assertNotNull(profile);

        mockMvc.perform(patch("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        profile = profileRepository.findById(1L).orElse(null);
        String newName = profile.getName();
        assertEquals("NewName", newName);

        profile.setName("asd");
        profileRepository.save(profile);
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void rateProfile() throws Exception {

        RateDto rateDto = new RateDto();
        rateDto.setRate(2.5d);
        rateDto.setProfileId(1L);

        mockMvc.perform(put("/profiles/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rateDto)))
                .andExpect(status().isOk());

        Profile profile = profileRepository.findById(1L).orElse(null);
        Double ratingValue = profile.getRatingValue();

        assertNotNull(profile);
        assertEquals(2.5d, ratingValue);

        ratingRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void getProfile() throws Exception {

        mockMvc.perform(get("/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("asd"))
                .andExpect(jsonPath("$.surname").value("asd"))
                .andExpect(jsonPath("$.phoneNumber").value("123123123"));

    }
}