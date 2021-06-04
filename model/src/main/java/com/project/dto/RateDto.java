package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RateDto {

    @NotBlank
    private String username;
    @NotNull
    private Long profileId;
    @NotNull
    private Double rate;

}
