package com.project.service;


import com.project.dto.*;
import org.springframework.data.domain.Page;

public interface IAdvertService {

    void createAdvert(CreateAdvertDto advertDto);
    void updateAdvert(UpdateAdvertDto advertDto);
    void deleteAdvert(DeleteAdvertDto advertDto);
    void enablePremiumStatus(Long advertId, Integer premDays);
    void addCommentaryToAdvert(CommentaryDto commentaryDto);
    void deleteComment(Long commentId);
    void editComment(EditCommentDto editCommentDto);
    Page<AdvertDto> getAdverts(AdvertListDto advertDto);
    Page<AdvertDto> sellingHistory(Long profileId, Integer page, Integer size);

}
