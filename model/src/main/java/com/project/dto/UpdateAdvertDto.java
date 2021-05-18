package com.project.dto;

import lombok.Data;

@Data
public class UpdateAdvertDto {

    private Long advertId;
    private String adName;
    private Double adPrice;
    private String description;

}
