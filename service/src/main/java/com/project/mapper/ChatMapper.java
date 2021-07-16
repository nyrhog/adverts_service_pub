package com.project.mapper;

import com.project.dto.ChatDto;
import com.project.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = MessageMapper.class)
public interface ChatMapper {

    ChatDto chatToChatDto(Chat chat);

}
