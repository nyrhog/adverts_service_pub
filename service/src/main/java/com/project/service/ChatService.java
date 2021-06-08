package com.project.service;

import com.project.dao.ChatRepository;
import com.project.dao.MessageRepository;
import com.project.dao.ProfileRepository;
import com.project.dto.CreateChatDto;
import com.project.dto.DeleteMessageDto;
import com.project.dto.SendMessageDto;
import com.project.dto.UpdateMessageDto;
import com.project.entity.Chat;
import com.project.entity.Message;
import com.project.entity.Profile;
import com.project.exceprion.InvalidUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private final ProfileRepository profileRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Transactional
    @Override
    public void createChat(CreateChatDto createChatDto) {

        Profile chatCreator = profileRepository.findById(createChatDto.getChatCreateProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatCreateProfileId())));

        Profile recipient = profileRepository.findById(createChatDto.getChatWithProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatCreateProfileId())));

        if (isChatExist(chatCreator, recipient)) {
            log.info("Chat is exist");
            return;
        }

        Chat chat = new Chat();
        chat.setProfiles(List.of(chatCreator, recipient));

        chatCreator.getChats().add(chat);
        recipient.getChats().add(chat);
    }

    @Transactional
    @Override
    public void sendMessage(SendMessageDto messageDto) {

        Profile messageSender = profileRepository.findById(messageDto.getSenderIdProfile())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, messageDto.getSenderIdProfile())));
        Chat chat = chatRepository.findById(messageDto.getChatId()).orElseThrow(EntityNotFoundException::new);

        Message message = new Message();
        message.setText(messageDto.getText());

        chat.getMessages().add(message);

        messageSender.getMessages().add(message);
        message.setProfile(messageSender);
    }


    protected boolean isChatExist(Profile creator, Profile recipient) {
        Chat chat = chatRepository.getChatByProfilesIdIn(creator.getId(), recipient.getId());
        return chat != null;
    }

    @Transactional
    @Override
    public void updateMessage(UpdateMessageDto updateMessageDto) {

        Message message = messageRepository.findById(updateMessageDto.getMessageId())
                .orElseThrow(EntityNotFoundException::new);

        String messageCreator = message.getProfile().getUser().getUsername();

        if(!updateMessageDto.getUsername().equals(messageCreator)){
            throw new InvalidUserException(String.format("Promoted user with username: %s is not a message creator", updateMessageDto.getUsername()));
        }

        if (message.getText().equals(updateMessageDto.getNewText())){
            return;
        }

        message.setText(updateMessageDto.getNewText());
    }

    @Transactional
    @Override
    public void deleteMessage(DeleteMessageDto deleteMessageDto) {
        Message message = messageRepository.findById(deleteMessageDto.getMessageId())
                .orElseThrow(EntityNotFoundException::new);

        String messageCreator = message.getProfile().getUser().getUsername();

        if(!deleteMessageDto.getUsername().equals(messageCreator)){
            throw new InvalidUserException(String.format("Promoted user with username: %s is not a message creator", deleteMessageDto.getUsername()));
        }

        messageRepository.delete(message);
    }
}
