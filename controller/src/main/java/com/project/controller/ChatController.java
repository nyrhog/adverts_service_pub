package com.project.controller;

import com.project.dto.CreateChatDto;
import com.project.dto.SendMessageDto;
import com.project.service.IChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final IChatService chatService;

    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<Void> createChat(@RequestBody @Valid CreateChatDto createChatDto){

        chatService.createChat(createChatDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid SendMessageDto sendMessageDto){

        chatService.sendMessage(sendMessageDto);
        return ResponseEntity.noContent().build();
    }

}
