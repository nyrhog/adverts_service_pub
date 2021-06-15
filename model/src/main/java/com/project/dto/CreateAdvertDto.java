package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAdvertDto {

    @NotNull
    private Long profileId;
    @NotBlank
    private String adName;
    @NotNull
    private Double adPrice;
    @NotBlank
    private String description;
    @NotEmpty
    private List<String> categories;
}
