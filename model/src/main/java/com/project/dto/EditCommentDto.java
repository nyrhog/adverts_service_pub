package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditCommentDto {

    @NotBlank
    private String username;
    @NotNull
    private Long commentId;
    @NotBlank
    private String newCommentText;

}
