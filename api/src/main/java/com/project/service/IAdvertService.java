package com.project.service;


import com.project.dto.*;
import org.springframework.data.domain.Page;

public interface IAdvertService {

    void createAdvert(CreateAdvertDto advertDto);
    void updateAdvert(UpdateAdvertDto advertDto);
    void deleteAdvert(DeleteAdvertDto advertDto);
    void enablePremiumStatus(Long advertId);
    void addCommentaryToAdvert(CommentaryDto commentaryDto);
    Page<AdvertDto> getAdverts(AdvertListDto advertDto);

}
