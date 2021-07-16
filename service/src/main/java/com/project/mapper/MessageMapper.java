package com.project.mapper;

import com.project.dto.MessageDto;
import com.project.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MessageMapper {

    @Mapping(target = "creatorProfileId", source = "message.profile.id")
    MessageDto toMessageDto(Message message);

}
