package com.project.service;


import com.project.dto.CommentaryDto;
import com.project.dto.CreateAdvertDto;
import com.project.dto.UpdateAdvertDto;

public interface IAdvertService {

    void createAdvert(CreateAdvertDto advertDto);
    void updateAdvert(UpdateAdvertDto advertDto);
    void deleteAdvert(Long id);
    void enablePremiumStatus(Long advertId);
    void addCommentaryToAdvert(CommentaryDto commentaryDto);

}
