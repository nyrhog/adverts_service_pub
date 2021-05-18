package com.project.service;

import com.project.dao.ProfileRepository;
import com.project.dto.CreateChatDto;
import com.project.entity.Chat;
import com.project.entity.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class ChatService implements IChatService {

    private final ProfileRepository profileRepository;

    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Autowired
    public ChatService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Transactional
    @Override
    public void createChat(CreateChatDto createChatDto) {

        Profile chatCreator = profileRepository.findById(createChatDto.getChatCreateProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatCreateProfileId())));

        Profile secondProfile = profileRepository.findById(createChatDto.getChatCreateProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, createChatDto.getChatCreateProfileId())));

        Chat chat = new Chat();
        chat.setProfiles(List.of(chatCreator, secondProfile));

        chatCreator.getChats().add(chat);
        secondProfile.getChats().add(chat);
    }
}
