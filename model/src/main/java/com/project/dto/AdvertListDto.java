package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AdvertListDto {

    @NotEmpty
    private List<String> categories;
    private String search;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer pageNumber;
}
