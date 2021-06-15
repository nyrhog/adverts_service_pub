package com.project.controller;

import com.project.dto.*;
import com.project.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;

    @PostMapping
    public ResponseEntity<Void> createChat(@RequestBody @Valid CreateChatDto createChatDto){

        chatService.createChat(createChatDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid SendMessageDto sendMessageDto){

        chatService.sendMessage(sendMessageDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> getChat(@PathVariable Long id){

        ChatDto chat = chatService.getChat(id);
        return ResponseEntity.ok(chat);
    }

    @PatchMapping
    public ResponseEntity<Void> updateMessage(@RequestBody @Valid UpdateMessageDto dto){

        chatService.updateMessage(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id){

        chatService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

}
