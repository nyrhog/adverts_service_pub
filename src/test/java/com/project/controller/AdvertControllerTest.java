package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.*;
import com.project.dto.*;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = AdvertsServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class AdvertControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final AdvertRepository advertRepository;
    private final ProfileRepository profileRepository;
    private final IUserService userService;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    private User user;
    private Category category;
    private Advert advert;

    @Autowired
    public AdvertControllerTest(AdvertRepository advertRepository, ProfileRepository profileRepository, IUserService userService, CategoryRepository categoryRepository, CommentRepository commentRepository) {
        this.advertRepository = advertRepository;
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
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

        user = new User();

        Profile profile = new Profile()
                .setName(requestDto.getFirstName())
                .setPhoneNumber(requestDto.getPhoneNumber())
                .setSurname(requestDto.getSurname());

        user.setPassword(requestDto.getPassword())
                .setUsername(requestDto.getUsername())
                .setEmail(requestDto.getEmail())
                .setProfile(profile);

        category = new Category();
        category.setCategoryName("asd");

        advert = new Advert();
        advert.setAdName("ad");
        advert.setAdPrice(123d);
        advert.setStatus(Status.ACTIVE);
        advert.setProfile(profile);
        advert.setDescription("not null");

        AdvertPremium advertPremium = new AdvertPremium();
        advertPremium.setIsActive(false);

        advert.setAdvertPremium(advertPremium);
        userService.register(user);
        advertRepository.save(advert);
        categoryRepository.save(category);
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void createAdvert() throws Exception {

        CreateAdvertDto advertDto = new CreateAdvertDto();
        advertDto.setAdName("adName");
        advertDto.setCategories(List.of("asd"));
        advertDto.setDescription("asdasdasd");
        advertDto.setAdPrice(123d);

        mockMvc.perform(MockMvcRequestBuilders.post("/adverts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advertDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Advert advert = advertRepository.findAll().get(1);

        Assertions.assertNotNull(advert);
        Assertions.assertEquals("adName", advert.getAdName());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void updateAdvert() throws Exception {

        categoryRepository.save(category);

        advert = advertRepository.findAll().get(0);

        UpdateAdvertDto advertDto = new UpdateAdvertDto();
        advertDto.setAdvertId(advert.getId());
        advertDto.setUsername("nyrhog");
        advertDto.setAdName("newAdName");

        mockMvc.perform(MockMvcRequestBuilders.patch("/adverts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advertDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        advert = advertRepository.findById(advert.getId()).orElse(null);

        Assertions.assertNotNull(advert);
        Assertions.assertEquals("newAdName", advert.getAdName());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void deleteAdvert() throws Exception {

        Long id = advert.getId();

        Assertions.assertEquals(1, advertRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/adverts/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertEquals(0, advertRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void addComment() throws Exception {

        Long id = advert.getId();

        CommentaryDto commentaryDto = new CommentaryDto();
        commentaryDto.setAdvertId(id);
        commentaryDto.setCommentaryMessage("Some comment");

        mockMvc.perform(MockMvcRequestBuilders.post("/adverts/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentaryDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Advert advert = advertRepository.findById(id).orElse(null);
        List<Comment> all = commentRepository.findAll();
        Assertions.assertEquals(1, all.size());
        Assertions.assertNotNull(advert);
        Assertions.assertEquals("Some comment", advert.getComments().get(0).getCommentText());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void updateComment() throws Exception {
        Profile profile = profileRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setCommentText("text");
        comment.setProfile(profile);
        comment.setAdvert(advert);
        comment = commentRepository.save(comment);

        EditCommentDto dto = new EditCommentDto();
        dto.setNewCommentText("newCommentText");
        dto.setCommentId(comment.getId());

        mockMvc.perform(MockMvcRequestBuilders.patch("/adverts/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals("newCommentText", comment.getCommentText());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void deleteComment() throws Exception {

        Profile profile = profileRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setCommentText("text");
        comment.setAdvert(advert);
        comment.setProfile(profile);

        commentRepository.save(comment);
        Assertions.assertEquals(1, commentRepository.findAll().size());

        mockMvc.perform(MockMvcRequestBuilders.delete("/adverts/comment?id=" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void getAdvert() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/adverts/" + advert.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(advert.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.adName").value("ad"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.adPrice").value(123d));

    }

}