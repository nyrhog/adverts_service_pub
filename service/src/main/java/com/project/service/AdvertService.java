package com.project.service;


import com.project.Logging;
import com.project.dao.*;
import com.project.dto.*;
import com.project.entity.*;
import com.project.mapper.AdvertMapper;
import com.project.mapper.BillingDetailsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final AdvertPremiumRepository advertPremiumRepository;
    private final CustomizedAdvertRepository<Advert> customizedAdvertRepository;
    private final BillingDetailsRepository billingDetailsRepository;
    private final BillingDetailsMapper billingDetailsMapper;
    private final AdvertMapper advertMapper;

    @Autowired
    private AdvertService advertService;

    @Value("${premiumDays}")
    private Integer premiumDays;

    @Value("${pricePerDay}")
    private int pricePerDay;

    private static final String ADVERT_NOT_FOUND = "Advert with id: %s not found";
    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";
    private static final String COMMENTARY_NOT_FOUND = "Commentary with id: %s not found";

    @Override
    @Transactional
    @Logging
    public Advert createAdvert(CreateAdvertDto advertDto) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

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


        Advert savedAdvert = advertsRepository.save(advert);
        profile.getAdverts().add(advert);

        return savedAdvert;
    }


    @Override
    @Transactional
    @Logging
    public Advert updateAdvert(UpdateAdvertDto advertDto) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Advert advert;

        if (UtilServiceClass.isAdmin(profile)) {
            advert = advertsRepository.findById(advertDto.getAdvertId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertDto.getAdvertId())));
            log.info("User is an admin");
        } else {
            advert = advertsRepository.getAdvertByIdAndProfile_User_Username(advertDto.getAdvertId(), currentPrincipalName)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertDto.getAdvertId())));
            log.info("User is not an admin");
        }

        advertMapper.updateAdvert(advert, advertDto);

        advert.setUpdated(LocalDateTime.now(ZoneId.of("Europe/Minsk")));

        return advert;
    }

    @Logging
    @Override
    @Transactional
    public void deleteAdvert(Long id) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Advert advert;

        if (UtilServiceClass.isAdmin(profile)) {
            advert = advertsRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, id)));
        } else {
            advert = advertsRepository.getAdvertByIdAndProfile_User_Username(id, currentPrincipalName)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, id)));
        }

        advertsRepository.delete(advert);
    }

    @Override
    @Transactional
    @Logging
    public Advert closeAdvert(Long id) {
        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Advert advert = advertsRepository.getAdvertByIdAndProfile_User_Username(id, currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, id)));

        advert.setStatus(Status.CLOSED);

        return advert;
    }

    @Override
    @Logging
    public List<AdvertDto> getProfileActiveAdverts(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, profileId)));

        List<Advert> adverts = advertsRepository.getAdvertsByProfileAndStatus(profile, Status.ACTIVE);

        return adverts.stream()
                .map(advertMapper::toAdvertDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Logging
    public AdvertPremium enablePremiumStatus(Long advertId) {
        Advert advert = advertsRepository.findById(advertId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertId)));

        Integer premDays = advert.getBillingDetails().getDays();

        if (premDays == null) {
            premDays = premiumDays;
        }

        advert.getAdvertPremium()
                .setIsActive(true)
                .setPremStarted(LocalDateTime.now())
                .setPremEnd(LocalDateTime.now().plusDays(premDays));

        billingDetailsRepository.delete(advert.getBillingDetails());

        return advert.getAdvertPremium();
    }

    @Override
    @Logging
    @Transactional
    public Comment addCommentaryToAdvert(CommentaryDto commentaryDto) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Advert advert = advertsRepository
                .findById(commentaryDto.getAdvertId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, commentaryDto.getAdvertId())));

        Profile sender = profileRepository
                .getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Comment comment = new Comment();
        comment.setCommentText(commentaryDto.getCommentaryMessage());
        comment.setAdvert(advert);
        comment.setProfile(sender);

        commentRepository.save(comment);

        return comment;
    }

    @Logging
    @Transactional
    public void deleteComment(Long commentId) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Comment comment;

        if (UtilServiceClass.isAdmin(profile)) {
            comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(COMMENTARY_NOT_FOUND, commentId)));
        } else {
            comment = commentRepository
                    .getCommentByIdAndProfile_User_Username(commentId, currentPrincipalName)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(COMMENTARY_NOT_FOUND, commentId)));
        }

        commentRepository.delete(comment);
    }

    @Transactional
    @Logging
    public Comment editComment(EditCommentDto editCommentDto) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Comment comment;

        if (UtilServiceClass.isAdmin(profile)) {
            comment = commentRepository.findById(editCommentDto.getCommentId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format(COMMENTARY_NOT_FOUND, editCommentDto.getCommentId())));
        } else {
            comment = commentRepository
                    .getCommentByIdAndProfile_User_Username(editCommentDto.getCommentId(), currentPrincipalName)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(COMMENTARY_NOT_FOUND, editCommentDto.getCommentId())));
        }

        comment.setCommentText(editCommentDto.getNewCommentText());

        return comment;
    }

    @Logging
    public Page<AdvertDto> getAdverts(AdvertListDto advertDto) {

        advertService.disableExpiredPrems();

        Integer pageNumber = advertDto.getPageNumber();
        Integer pageSize = advertDto.getPageSize();
        String search = advertDto.getSearch();

//        List<String> replacedCategories = replaceCharsInCategories(advertDto.getCategories());

        List<Category> categories = categoryRepository.findByCategoryNameIn(advertDto.getCategories());
        List<String> categoriesNames = categories.stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        Page<Advert> adverts = customizedAdvertRepository.findAllByCategoriesIn(categoriesNames, search,
                PageRequest.of(pageNumber, pageSize));

        List<AdvertDto> advertDtoList = adverts.getContent()
                .stream()
                .map((advertMapper::toAdvertDto))
                .collect(Collectors.toList());

        return new PageImpl<>(advertDtoList, PageRequest.of(pageNumber, pageSize), adverts.getTotalElements());
    }

    private List<String> replaceCharsInCategories(List<String> categories) {
        return categories.stream()
                .map(category -> category.replace("_", " "))
                .collect(Collectors.toList());
    }

    @Transactional
    @Logging
    public void disableExpiredPrems() {
        List<AdvertPremium> expiredPrems = advertPremiumRepository.getAllByPremEndLessThanAndIsActiveTrue(LocalDateTime.now());

        expiredPrems.forEach((advertPremium -> {
            advertPremium.setIsActive(false);
            advertPremium.setPremStarted(null);
        }));

        log.info("Expired premiums was deactivated");
    }

    @Override
    @Logging
    public Page<AdvertDto> sellingHistory(Long profileId, Integer page, Integer size) {

        Page<Advert> advertList = advertsRepository.getAllClosedAdvertsWithProfileId(profileId,
                PageRequest.of(page, size, Sort.by(Advert_.CLOSED).descending()));

        List<AdvertDto> advertListDto = advertList.getContent().stream()
                .map(advertMapper::toAdvertDto)
                .collect(Collectors.toList());

        return new PageImpl<>(advertListDto, PageRequest.of(page, size), advertList.getTotalElements());
    }

    @Override
    @Logging
    public AdvertDto getOneAdvert(Long advertId) {
        Advert advert = advertsRepository.findById(advertId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertId)));

        return advertMapper.toAdvertDto(advert);
    }

    @Override
    @Transactional
    @Logging
    public BillingDetailsDto getBillingDetails(Long advertId, Integer days) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Advert advert = advertsRepository.getAdvertByIdAndProfile_User_Username(advertId, currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADVERT_NOT_FOUND, advertId)));

        BillingDetails details;
        String billingCount;

        if (advert.getBillingDetails() != null) {
            return billingDetailsMapper.toBillingDetailsDto(advert.getBillingDetails());
        }

        do {
            billingCount = generateBillingCount().toString();
            details = billingDetailsRepository.findBillingDetailsByPaymentCount(billingCount);
            if (details == null) {
                break;
            }
        } while (details.getPaymentCount().equals(billingCount));

        BillingDetails billingDetails = new BillingDetails();
        billingDetails.setPaymentCount(billingCount);
        billingDetails.setAdvert(advert);
        billingDetails.setPrice((double) (pricePerDay * days));
        billingDetails.setDays(days);

        advert.setBillingDetails(billingDetails);

        return billingDetailsMapper.toBillingDetailsDto(billingDetails);
    }

    private Long generateBillingCount() {
        long leftLimit = 1_000_000_000_000L;
        long rightLimit = 9_999_999_999_999L;

        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
