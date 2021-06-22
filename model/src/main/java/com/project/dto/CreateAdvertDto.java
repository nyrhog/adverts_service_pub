package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAdvertDto {

    @NotBlank(message = "Required advert name!")
    private String adName;
    @NotNull(message = "Required advert price!")
    private Double adPrice;
    @NotBlank(message = "Required description!")
    private String description;
    @NotEmpty(message = "Required at least one category!")
    private List<String> categories;
}
