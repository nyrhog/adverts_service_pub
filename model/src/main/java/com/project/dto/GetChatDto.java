package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GetChatDto {

   @NotBlank
   private String username;
   @NotNull
   private Long chatId;
}
