package com.project.service;

import com.project.dto.CreateChatDto;
import com.project.dto.DeleteMessageDto;
import com.project.dto.SendMessageDto;
import com.project.dto.UpdateMessageDto;

public interface IChatService {

    void createChat(CreateChatDto createChatDto);
    void sendMessage(SendMessageDto messageDto);
    void updateMessage(UpdateMessageDto updateMessageDto);
    void deleteMessage(DeleteMessageDto deleteMessageDto);
}
