package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentaryDto {

    @NotNull(message = "Required advert id!")
    private Long advertId;
    @NotBlank(message = "Required commentary text!")
    private String commentaryMessage;

}
