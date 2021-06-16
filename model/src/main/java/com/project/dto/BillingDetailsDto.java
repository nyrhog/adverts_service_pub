package com.project.dto;

import lombok.Data;

@Data
public class BillingDetailsDto {

    private String paymentCount;
    private Double price;
    private Integer days;

}
