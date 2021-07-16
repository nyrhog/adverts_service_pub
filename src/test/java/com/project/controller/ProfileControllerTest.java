package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.*;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.dto.RegistrationDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = AdvertsServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Transactional
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
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName("asd");
        registrationDto.setSurname("asd");
        registrationDto.setPassword("asd");
        registrationDto.setEmail("xxx@mail.ru");
        registrationDto.setUsername("nyrhog");
        registrationDto.setPhoneNumber("123123123");

        RegistrationDto registrationDto2 = new RegistrationDto();
        registrationDto2.setFirstName("asd");
        registrationDto2.setSurname("asd");
        registrationDto2.setPassword("asd");
        registrationDto2.setEmail("xxx2@mail.ru");
        registrationDto2.setUsername("nyrhog2");
        registrationDto2.setPhoneNumber("123123123");

        registerUser(registrationDto);
        registerUser(registrationDto2);
    }

    private void registerUser(RegistrationDto registrationDto){
        User user = new User();
        Profile profile = new Profile()
                .setName(registrationDto.getFirstName())
                .setPhoneNumber(registrationDto.getPhoneNumber())
                .setSurname(registrationDto.getSurname());

        user.setPassword(registrationDto.getPassword())
                .setUsername(registrationDto.getUsername())
                .setEmail(registrationDto.getEmail())
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
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void rateProfile() throws Exception {

        RateDto rateDto = new RateDto();
        rateDto.setRate(2.5d);
        rateDto.setProfileId(2L);

        mockMvc.perform(put("/profiles/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rateDto)))
                .andExpect(status().isCreated());

        Profile profileSender = profileRepository.getById(1L);
        Profile profileRecipient = profileRepository.getById(2L);

        Rating rating = ratingRepository.getRatingByProfileSenderAndProfileRecipient(profileSender, profileRecipient);

        assertNotNull(rating);
        assertEquals(2.5d, rating.getRating());

    }

    @Test
    @WithMockUser(username = "nyrhog")
    void getProfile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("asd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.surname").value("asd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber").value("123123123"));

    }
}