package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditCommentDto {

    @NotNull(message = "Required comment id!")
    private Long commentId;
    @NotBlank(message = "Required new comment text!")
    private String newCommentText;

}
