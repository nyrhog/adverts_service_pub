package com.project.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AdvertPremiumDto {

    private Boolean isActive;
    private LocalDateTime premStarted;
    private LocalDateTime premEnd;

}
