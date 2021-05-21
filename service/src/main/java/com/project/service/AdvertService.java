package com.project.service;


import com.project.dao.*;
import com.project.dto.*;
import com.project.entity.*;
import com.project.mapper.AdvertMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertService implements IAdvertService {

    private final ProfileRepository profileRepository;
    private final AdvertRepository advertsRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final CustomizedAdvertRepository<Advert> advertCustomizedAdvertRepository;
    private final AdvertMapper mapper;

    @Value("${premiumDays}")
    private Long premiumDays;

    private static final String ADVERT_NOT_FOUND = "Advert with id: %s not found";
    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Transactional
    @Override
    public void createAdvert(CreateAdvertDto advertDto) {

        Profile profile = profileRepository.findById(advertDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, advertDto.getProfileId())));

        List<Category> categories = categoryRepository.findByCategoryNameIn(advertDto.getCategories());

        AdvertPremium advertPremium = new AdvertPremium()
                .setIsActive(false);

        Advert advert = new Advert()
                .setAdName(advertDto.getAdName())
                .setAdPrice(advertDto.getAdPrice())
                .setDescription(advertDto.getDescription())
                .setAdvertPremium(advertPremium)
                .setCategories(categories)
                .setStatus(Status.ACTIVE);

        categories.forEach(category -> category.getAdverts().add(advert));

        advertsRepository.saveAndFlush(advert);

        profile.getAdverts().add(advert);
    }

    @Transactional
    @Override
    public void updateAdvert(UpdateAdvertDto advertDto) {
        Advert advert = advertsRepository.findById(advertDto.getAdvertId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertDto.getAdvertId())));

        String adName = advertDto.getAdName();
        Double adPrice = advertDto.getAdPrice();
        String description = advertDto.getDescription();

        if(adName != null){
            advert.setAdName(adName);
        }

        if(adPrice != null){
            advert.setAdPrice(adPrice);
        }

        if(description != null){
            advert.setDescription(description);
        }

        advert.setUpdated(LocalDateTime.now(ZoneId.of("Europe/Minsk")));
    }

    @Transactional
    @Override
    public void deleteAdvert(DeleteAdvertDto advertDto) {
        Advert advert = advertsRepository.findById(advertDto.getAdvertId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertDto.getAdvertId())));

        DeleteAdvertDto returnAdvertDto = new DeleteAdvertDto();
        returnAdvertDto.setUsername(advert.getProfile().getUser().getUsername());
        returnAdvertDto.setAdvertId(advertDto.getAdvertId());

        advertsRepository.delete(advert);

    }

    @Transactional
    @Override
    public void enablePremiumStatus(Long advertId) {
        Advert advert = advertsRepository.findById(advertId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertId)));

        advert.getAdvertPremium()
                .setIsActive(true)
                .setPremStarted(LocalDateTime.now())
                .setPremEnd(LocalDateTime.now().plusDays(premiumDays));
    }

    @Transactional
    @Override
    public void addCommentaryToAdvert(CommentaryDto commentaryDto) {
        Advert advert = advertsRepository.findById(commentaryDto.getAdvertId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, commentaryDto.getAdvertId())));

        Profile sender = profileRepository.findById(commentaryDto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, commentaryDto.getSenderId())));

        Comment comment = new Comment();
        comment.setCommentText(commentaryDto.getCommentaryMessage());
        comment.setAdvert(advert);
        comment.setProfile(sender);

        sender.getComments().add(comment);
        advert.getComments().add(comment);

    }

    //todo add to interface
    @Transactional
    public void deleteComment(Long commentId){
        //todo exception message
       Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

       comment.getAdvert().getComments().remove(comment);
       comment.getProfile().getComments().remove(comment);

       commentRepository.delete(comment);
    }

    //todo add to interface
    @Transactional
    public void editComment(EditCommentDto editCommentDto){
        Comment comment = commentRepository.findById(editCommentDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException());
        comment.setCommentText(editCommentDto.getNewCommentText());
    }

    public Page<AdvertDto> getAdverts(AdvertListDto advertDto){

        Integer pageNumber = advertDto.getPageNumber();
        Integer pageSize = advertDto.getPageSize();

        List<String> replacedCategories = replaceCharsInCategories(advertDto.getCategories());

        List<Category> categories = categoryRepository.findByCategoryNameIn(replacedCategories);
        List<String> categoriesNames = categories.stream().map(Category::getCategoryName).collect(Collectors.toList());

        Page<Advert> adverts = advertCustomizedAdvertRepository.findAllByCategoriesIn(categoriesNames, PageRequest.of(pageNumber, pageSize));

        List<AdvertDto> advertDtoList = adverts.getContent()
                .stream()
                .map((mapper::toAdvertDto))
                .collect(Collectors.toList());

        return new PageImpl<>(advertDtoList, PageRequest.of(pageNumber, pageSize), advertDtoList.size());
    }

    protected List<String> replaceCharsInCategories(List<String> categories){
        return categories.stream()
                .map(category -> category.replace("_", " "))
                .collect(Collectors.toList());
    }


}
