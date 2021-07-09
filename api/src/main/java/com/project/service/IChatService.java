package com.project.service;

import com.project.dto.*;
import com.project.entity.Chat;
import com.project.entity.Message;

public interface IChatService {

    Chat createChat(CreateChatDto createChatDto);
    Message sendMessage(SendMessageDto messageDto);
    Message updateMessage(UpdateMessageDto updateMessageDto);
    void deleteMessage(Long id);
    ChatDto getChat(Long id);
}
