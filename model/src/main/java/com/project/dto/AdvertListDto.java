package com.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdvertListDto {

    private List<String> categories;
    private Integer pageSize;
    private Integer pageNumber;


}
