package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AdvertListDto {

    @NotEmpty(message = "Required at least one category!")
    private List<String> categories;
    private String search;
    @NotNull(message = "Required page size")
    private Integer pageSize;
    @NotNull(message = "Required page number")
    private Integer pageNumber;
}
