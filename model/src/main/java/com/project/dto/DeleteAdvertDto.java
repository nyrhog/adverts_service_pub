package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeleteAdvertDto {

    @NotBlank
    private String username;

    @NotNull
    private Long advertId;

}
