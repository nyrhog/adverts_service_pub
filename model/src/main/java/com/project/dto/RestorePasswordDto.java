package com.project.dto;

import lombok.Data;

@Data
public class RestorePasswordDto {

    private String username;
    private int code;
    private String newPassword;

}
