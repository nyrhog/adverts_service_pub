package com.project.controller;

import com.project.dto.*;
import com.project.service.IChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("#createChatDto.username == authentication.principal.username")
    public ResponseEntity<Void> createChat(@RequestBody @Valid CreateChatDto createChatDto){

        chatService.createChat(createChatDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/message")
    @PreAuthorize("#sendMessageDto.username == authentication.principal.username")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid SendMessageDto sendMessageDto){

        chatService.sendMessage(sendMessageDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("#dto.username == authentication.principal.username")
    public ResponseEntity<ChatDto> getChat(@RequestBody @Valid GetChatDto dto){

        ChatDto chat = chatService.getChat(dto);
        return ResponseEntity.ok(chat);
    }

    @PatchMapping
    @PreAuthorize("#dto.username == authentication.principal.username")
    public ResponseEntity<Void> updateMessage(@RequestBody @Valid UpdateMessageDto dto){

        chatService.updateMessage(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("#dto.username == authentication.principal.username")
    public ResponseEntity<Void> deleteMessage(@RequestBody @Valid DeleteMessageDto dto){

        chatService.deleteMessage(dto);
        return ResponseEntity.ok().build();
    }

}
