package com.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateAdvertDto {
    private Long profileId;
    private String adName;
    private Double adPrice;
    private String description;

    private List<String> categories;
}
