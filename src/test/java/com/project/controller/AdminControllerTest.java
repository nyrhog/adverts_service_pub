package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.AdvertRepository;
import com.project.dao.RoleRepository;
import com.project.dao.UserRepository;
import com.project.dto.RegistrationDto;
import com.project.entity.*;
import com.project.enums.Status;
import com.project.service.IUserService;
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
    private final IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public AdminControllerTest(AdvertRepository advertRepository, RoleRepository roleRepository, UserRepository userRepository, IUserService userService) {
        this.advertRepository = advertRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

        saveRoles();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        User user = userRepository.findByUsername("nyrhog").orElse(null);

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
    }

    private void saveRoles(){
        Role roleAdmin = new Role();
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleAdmin.setName("ROLE_ADMIN");

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "ADMIN")
    void enablePremStatus() throws Exception {

        saveRoles();
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("nyrhog");
        registrationDto.setPassword("test");
        registrationDto.setEmail("test@gmail.com");
        registrationDto.setFirstName("Kirill");
        registrationDto.setSurname("Kananovich");
        registrationDto.setPhoneNumber("335553535");

        User user = new User();

        Profile profile = new Profile()
                .setName(registrationDto.getFirstName())
                .setPhoneNumber(registrationDto.getPhoneNumber())
                .setSurname(registrationDto.getSurname());

        user.setPassword(registrationDto.getPassword())
                .setUsername(registrationDto.getUsername())
                .setEmail(registrationDto.getEmail())
                .setProfile(profile);

        User adminUser = userService.registerAdminUser(user);

        AdvertPremium advertPremium = new AdvertPremium();

        Advert advert = new Advert();
        advert.setAdName("ad");
        advert.setAdPrice(123d);
        advert.setStatus(Status.ACTIVE);
        advert.setDescription("Not null");
        advert.setAdvertPremium(advertPremium);
        advert.setProfile(adminUser.getProfile());
        advert = advertRepository.save(advert);

        BillingDetails billingDetails = new BillingDetails();
        billingDetails.setDays(4);
        billingDetails.setAdvert(advert);

        advert.setBillingDetails(billingDetails);
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/premium/" + advert.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        advert = advertRepository.getById(advert.getId());
        Assertions.assertTrue(advert.getAdvertPremium().getIsActive());

    }
}