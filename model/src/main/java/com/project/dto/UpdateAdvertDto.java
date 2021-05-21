package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAdvertDto {

    @NotNull
    private String username;
    @NotNull
    private Long advertId;
    private String adName;
    private Double adPrice;
    private String description;

}
