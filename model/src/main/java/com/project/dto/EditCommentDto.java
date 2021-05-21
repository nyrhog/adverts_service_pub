package com.project.dto;

import lombok.Data;

@Data
public class EditCommentDto {

    private Long commentId;
    private String newCommentText;

}
