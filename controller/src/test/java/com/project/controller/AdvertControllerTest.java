package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.*;
import com.project.dto.*;
import com.project.entity.*;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

        userService.register(user);
        categoryRepository.save(category);
    }


    @AfterEach
    private void resetDb() {
        advertRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void createAdvert() throws Exception {

        CreateAdvertDto advertDto = new CreateAdvertDto();
        advertDto.setAdName("adName");
        advertDto.setCategories(List.of("asd"));
        advertDto.setDescription("asdasdasd");
        advertDto.setAdPrice(123d);
        advertDto.setProfileId(1L);

        mockMvc.perform(post("/adverts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advertDto)))
                .andExpect(status().isNoContent());

        Advert advert = advertRepository.findAll().get(0);

        assertNotNull(advert);
        assertEquals("adName", advert.getAdName());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void updateAdvert() throws Exception {

        userService.register(user);
        categoryRepository.save(category);

        saveAdvert();

        Advert advert = advertRepository.findAll().get(0);

        UpdateAdvertDto advertDto = new UpdateAdvertDto();
        advertDto.setAdvertId(advert.getId());
        advertDto.setUsername("nyrhog");
        advertDto.setAdName("newAdName");

        mockMvc.perform(patch("/adverts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advertDto)))
                .andExpect(status().isOk());

        advert = advertRepository.findById(advert.getId()).orElse(null);

        assertNotNull(advert);
        assertEquals("newAdName", advert.getAdName());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void deleteAdvert() throws Exception {

        Advert advert = saveAdvert();

        DeleteAdvertDto advertDto = new DeleteAdvertDto();
        advertDto.setAdvertId(advert.getId());
        advertDto.setUsername("nyrhog");

        assertEquals(1, advertRepository.findAll().size());

        mockMvc.perform(delete("/adverts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advertDto)))
                .andExpect(status().isOk());

        assertEquals(0, advertRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    @Transactional
    void addComment() throws Exception {

        Long id = saveAdvert().getId();

        CommentaryDto commentaryDto = new CommentaryDto();
        commentaryDto.setAdvertId(id);
        commentaryDto.setCommentaryMessage("Some comment");

        mockMvc.perform(post("/adverts/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentaryDto)))
                .andExpect(status().isOk());

        Advert advert = advertRepository.findById(id).orElse(null);
        List<Comment> all = commentRepository.findAll();
        assertEquals(1, all.size());
        assertNotNull(advert);
        assertEquals("Some comment", advert.getComments().get(0).getCommentText());
    }

    @Test
    @Transactional
    @WithMockUser(username = "nyrhog")
    void updateComment() throws Exception {
        Profile profile = profileRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setCommentText("text");
        comment.setProfile(profile);
        comment = commentRepository.save(comment);

        EditCommentDto dto = new EditCommentDto();
        dto.setUsername("nyrhog");
        dto.setNewCommentText("newCommentText");
        dto.setCommentId(comment.getId());

        mockMvc.perform(patch("/adverts/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        assertEquals("newCommentText", comment.getCommentText());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void deleteComment() throws Exception {

        Advert advert = saveAdvert();
        Profile profile = profileRepository.findAll().get(0);

        Comment comment = new Comment();
        comment.setCommentText("text");
        comment.setAdvert(advert);
        comment.setProfile(profile);

        profile.setComments(List.of(comment));
        advert.setComments(List.of(comment));

        assertEquals(0, commentRepository.findAll().size());

        comment = commentRepository.save(comment);
        profileRepository.save(profile);
        advertRepository.save(advert);

        assertEquals(1, commentRepository.findAll().size());
        mockMvc.perform(delete("/adverts/comment?username=nyrhog&id=" + comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void getAdvert() throws Exception {
        Advert advert = saveAdvert();

        mockMvc.perform(get("/adverts/" + advert.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(advert.getId()))
                .andExpect(jsonPath("$.adName").value("ad"))
                .andExpect(jsonPath("$.adPrice").value(123d));

    }

    private Advert saveAdvert() {
        Profile profile = profileRepository.findAll().get(0);

        Advert advert = new Advert();
        advert.setAdName("ad");
        advert.setAdPrice(123d);
        advert.setStatus(Status.ACTIVE);
        advert.setProfile(profile);

        return advertRepository.save(advert);
    }
}