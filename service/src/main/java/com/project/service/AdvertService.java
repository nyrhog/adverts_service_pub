package com.project.service;

import com.project.dao.AdvertsRepository;
import com.project.dao.CategoryRepository;
import com.project.dao.CommentRepository;
import com.project.dao.ProfileRepository;
import com.project.dto.CommentaryDto;
import com.project.dto.CreateAdvertDto;
import com.project.dto.UpdateAdvertDto;
import com.project.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdvertService implements IAdvertService {

    private final ProfileRepository profileRepository;
    private final AdvertsRepository advertsRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    @Value("${premiumDays}")
    private Long premiumDays;

    private static final String ADVERT_NOT_FOUND = "Advert with id: %s not found";
    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Autowired
    public AdvertService(ProfileRepository profileRepository, AdvertsRepository advertsRepository, CategoryRepository categoryRepository, CommentRepository commentRepository) {
        this.profileRepository = profileRepository;
        this.advertsRepository = advertsRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
    }

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

    }

    @Transactional
    @Override
    public void deleteAdvert(Long advertId) {
        Advert advert = advertsRepository.findById(advertId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertId)));

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

        advert.getComments().add(comment);
        sender.getComments().add(comment);
    }


    public void deleteComment(Long commentId){
        //todo exception message
       Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());

       comment.getAdvert().getComments().remove(comment);
       comment.getProfile().getComments().remove(comment);

       commentRepository.delete(comment);
    }


}
