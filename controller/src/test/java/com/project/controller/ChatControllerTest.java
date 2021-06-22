package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AdvertsServiceApplication;
import com.project.dao.*;
import com.project.dto.CreateChatDto;
import com.project.dto.RegistrationDto;
import com.project.dto.SendMessageDto;
import com.project.entity.Chat;
import com.project.entity.Message;
import com.project.entity.Profile;
import com.project.entity.User;
import com.project.service.IChatService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = AdvertsServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final ChatRepository chatRepository;
    private final IUserService userService;
    private final IChatService chatService;
    private final MessageRepository messageRepository;


    @Autowired
    public ChatControllerTest(ChatRepository chatRepository, IUserService userService, IChatService chatService, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userService = userService;
        this.chatService = chatService;
        this.messageRepository = messageRepository;
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

        registerUser(requestDto);

        requestDto = new RegistrationDto();
        requestDto.setFirstName("vtoroy");
        requestDto.setSurname("vtoroy");
        requestDto.setPassword("vtoroy");
        requestDto.setEmail("xxx2@mail.ru");
        requestDto.setUsername("rertyp");
        requestDto.setPhoneNumber("123123123");

        registerUser(requestDto);
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void createChat() throws Exception {

        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setChatCreateProfileId(1L);
        createChatDto.setChatWithProfileId(2L);

        mockMvc.perform(post("/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createChatDto)))
                .andExpect(status().isOk());

        Chat chat = chatRepository.getChatByProfilesIdIn(1L, 2L);
        assertNotNull(chat);
    }

    @Test
    @WithMockUser(username = "nyrhog")
    void sendMessage() throws Exception {

        SendMessageDto sendMessageDto = new SendMessageDto();
        sendMessageDto.setRecipientIdProfile(2L);
        sendMessageDto.setText("Message");

        createTestChat();

        mockMvc.perform(put("/chats/message")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendMessageDto)))
                .andExpect(status().isOk());

        List<Message> messages = messageRepository.findAll();

        assertEquals(1, messages.size());
        assertEquals("Message", messages.get(0).getText());
    }

    @Test
    @WithMockUser(username = "nyrhog")
     void getChat() throws Exception {

        createTestChat();

        mockMvc.perform(get("/chats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    void registerUser(RegistrationDto requestDto) {

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

    void createTestChat() {

        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setChatCreateProfileId(1L);
        createChatDto.setChatWithProfileId(2L);

        chatService.createChat(createChatDto);
    }

}