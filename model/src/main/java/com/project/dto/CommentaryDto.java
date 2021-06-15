package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentaryDto {

    @NotNull
    private Long advertId;
    @NotBlank
    private String commentaryMessage;

}
