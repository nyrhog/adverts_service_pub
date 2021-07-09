package com.project.service;

import com.project.Logging;
import com.project.dao.ChatRepository;
import com.project.dao.MessageRepository;
import com.project.dao.ProfileRepository;
import com.project.dto.*;
import com.project.entity.Chat;
import com.project.entity.Message;
import com.project.entity.Profile;
import com.project.exception.InvalidUserException;
import com.project.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private final ProfileRepository profileRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper mapper;

    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Transactional
    @Override
    @Logging
    public Chat createChat(CreateChatDto createChatDto) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile chatCreator = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatCreateProfileId())));

        Profile recipient = profileRepository.findById(createChatDto.getChatWithProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatWithProfileId())));

        if (isChatExist(chatCreator, recipient)) {
            log.info("Chat between {} and {} is exist", chatCreator.getName(), recipient.getName());
            return null;
        }

        Chat chat = new Chat();
        chat.setProfiles(List.of(chatCreator, recipient));

        return chat;
    }

    @Transactional
    @Override
    @Logging
    public Message sendMessage(SendMessageDto messageDto) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile messageSender = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Chat chat = chatRepository.getChatByProfilesIdIn(messageSender.getId(), messageDto.getRecipientIdProfile());

        Message message = new Message();
        message.setText(messageDto.getText());
        message.setChat(chat);

        chat.getMessages().add(message);

        messageSender.getMessages().add(message);
        message.setProfile(messageSender);

        log.info("Message was send");

        return message;
    }

    protected boolean isChatExist(Profile creator, Profile recipient) {
        Chat chat = chatRepository.getChatByProfilesIdIn(creator.getId(), recipient.getId());
        return chat != null;
    }

    @Transactional
    @Override
    @Logging
    public Message updateMessage(UpdateMessageDto updateMessageDto) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Message message = messageRepository.findById(updateMessageDto.getMessageId())
                .orElseThrow(EntityNotFoundException::new);

        String messageCreator = message.getProfile().getUser().getUsername();

        if (!currentPrincipalName.equals(messageCreator)) {
            throw new InvalidUserException("Promoted user with username: %s is not a message creator");
        }

        if (message.getText().equals(updateMessageDto.getNewText())) {
            return null;
        }

        message.setText(updateMessageDto.getNewText());
        return message;

    }

    @Override
    @Logging
    public void deleteMessage(Long id) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Message message = messageRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        String messageCreator = messageRepository.getMessageSenderUsername(message.getId());

        if (!currentPrincipalName.equals(messageCreator)) {
            throw new InvalidUserException(String.format("Promoted user with username: %s is not a message creator", currentPrincipalName));
        }

        messageRepository.delete(message);
    }

    @Transactional
    @Override
    @Logging
    public ChatDto getChat(Long id) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile noy found"));

        Chat chat = chatRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        boolean contains = profile.getChats().contains(chat);

        if (contains){
            return mapper.chatToChatDto(chat);
        }

        throw new EntityNotFoundException("This profile doesn't have current chat");
    }

}
