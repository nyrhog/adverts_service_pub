package com.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExceptionDto {

    private String exception;
    private String message;

}
