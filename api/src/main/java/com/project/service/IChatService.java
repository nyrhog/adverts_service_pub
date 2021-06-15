package com.project.service;

import com.project.dto.*;

public interface IChatService {

    void createChat(CreateChatDto createChatDto);
    void sendMessage(SendMessageDto messageDto);
    void updateMessage(UpdateMessageDto updateMessageDto);
    void deleteMessage(Long id);
    ChatDto getChat(Long id);
}
