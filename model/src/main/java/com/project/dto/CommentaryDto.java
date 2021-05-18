package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentaryDto {

    @NotNull
    private Long senderId;
    @NotNull
    private Long advertId;
    @NotNull
    private String commentaryMessage;

}
