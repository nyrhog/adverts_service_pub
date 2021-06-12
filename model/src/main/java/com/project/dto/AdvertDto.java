package com.project.dto;

import com.project.entity.Status;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdvertDto {

    private Long id;
    private String adName;
    private Double adPrice;
    private Status status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalDateTime closed;
    private List<CategoryDto> categories;
    private List<CommentDto> comments;


}
