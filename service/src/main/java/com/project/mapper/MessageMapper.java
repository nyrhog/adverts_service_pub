package com.project.mapper;

import com.project.dto.MessageDto;
import com.project.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MessageMapper {

    MessageDto toMessageDto(Message message);

}
