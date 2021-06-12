package com.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatDto {

    List<MessageDto> messages;

}
