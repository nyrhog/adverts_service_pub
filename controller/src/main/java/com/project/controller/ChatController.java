package com.project.controller;

import com.project.dto.*;
import com.project.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid SendMessageDto sendMessageDto){

        chatService.sendMessage(sendMessageDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponseDto> getChat(@PathVariable Long id){

        ChatDto chat = chatService.getChat(id);
        GlobalResponseDto globalResponseDto = new GlobalResponseDto(chat);
        return ResponseEntity.ok(globalResponseDto);
    }


    @PatchMapping
    public ResponseEntity<GlobalResponseDto> updateMessage(@RequestBody @Valid UpdateMessageDto dto){

        chatService.updateMessage(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id){

        chatService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

}
