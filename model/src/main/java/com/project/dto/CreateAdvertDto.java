package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAdvertDto {

    @NotNull
    private Long profileId;
    @NotNull
    private String adName;
    @NotNull
    private Double adPrice;
    @NotNull
    private String description;
    @NotEmpty
    private List<String> categories;
}
