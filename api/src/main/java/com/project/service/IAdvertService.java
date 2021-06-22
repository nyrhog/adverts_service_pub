package com.project.service;


import com.project.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAdvertService {

    void createAdvert(CreateAdvertDto advertDto);
    void updateAdvert(UpdateAdvertDto advertDto);
    void deleteAdvert(Long id);
    void closeAdvert(Long id);
    List<AdvertDto> getProfileActiveAdverts(Long id);
    void enablePremiumStatus(Long advertId);
    void addCommentaryToAdvert(CommentaryDto commentaryDto);
    void deleteComment(Long commentId);
    void editComment(EditCommentDto editCommentDto);
    Page<AdvertDto> getAdverts(AdvertListDto advertDto);
    Page<AdvertDto> sellingHistory(Long profileId, Integer page, Integer size);
    AdvertDto getOneAdvert(Long advertId);
    BillingDetailsDto getBillingDetails(Long id, Integer days);

}
