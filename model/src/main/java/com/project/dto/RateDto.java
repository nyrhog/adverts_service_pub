package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RateDto {

    @NotNull(message = "Required profile id")
    private Long profileId;
    @NotNull(message = "Required rate value")
    private Double rate;

}
