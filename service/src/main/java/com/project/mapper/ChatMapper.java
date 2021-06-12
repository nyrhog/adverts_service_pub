package com.project.mapper;

import com.project.dto.ChatDto;
import com.project.entity.Chat;
import org.mapstruct.Mapper;

@Mapper
public interface ChatMapper {


    ChatDto toChatDto(Chat chat);

}
