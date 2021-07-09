package com.project.service;


import com.project.dto.*;
import com.project.entity.Advert;
import com.project.entity.AdvertPremium;
import com.project.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAdvertService {

    Advert createAdvert(CreateAdvertDto advertDto);
    Advert updateAdvert(UpdateAdvertDto advertDto);
    void deleteAdvert(Long id);
    Advert closeAdvert(Long id);
    List<AdvertDto> getProfileActiveAdverts(Long id);
    AdvertPremium enablePremiumStatus(Long advertId);
    Comment addCommentaryToAdvert(CommentaryDto commentaryDto);
    void deleteComment(Long commentId);
    Comment editComment(EditCommentDto editCommentDto);
    Page<AdvertDto> getAdverts(AdvertListDto advertDto);
    Page<AdvertDto> sellingHistory(Long profileId, Integer page, Integer size);
    AdvertDto getOneAdvert(Long advertId);
    BillingDetailsDto getBillingDetails(Long id, Integer days);

}
