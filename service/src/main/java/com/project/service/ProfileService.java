package com.project.service;

import com.project.dao.ProfileRepository;
import com.project.dao.RatingRepository;
import com.project.dto.ProfileDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.entity.Profile;
import com.project.entity.Rating;
import com.project.exception.RateException;
import com.project.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final RatingRepository ratingRepository;
    private final ProfileMapper mapper;

    private static final String PROFILE_NOT_FOUND = "Profile with id: %s not found";

    @Transactional
    @Override
    public void updateProfile(ProfileUpdateDto updateDto) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profile = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, updateDto.getId())));

        if (UtilServiceClass.isAdmin(profile)) {
            profile = profileRepository.findById(updateDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, updateDto.getId())));
        }

        mapper.updateProfile(profile, updateDto);
        profile.setUpdated(LocalDateTime.now(ZoneId.of("Europe/Minsk")));

        log.info("Profile with id:{} was updated", profile.getId());
    }

    @Transactional
    @Override
    public void rateProfile(RateDto rateDto) {

        String currentPrincipalName = UtilServiceClass.getCurrentPrincipalName();

        Profile profileSender = profileRepository.getProfileByUserUsername(currentPrincipalName)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Profile profileRecipient = profileRepository.findById(rateDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, rateDto.getProfileId())));

        if (profileSender.equals(profileRecipient)) {
            throw new RateException("You cannot rate yourself");
        }

        Rating isRatingExist = ratingRepository.getRatingByProfileSenderAndProfileRecipient(profileSender, profileRecipient);

        if (isRatingExist != null) {
            throw new RateException("You are already rate this profile");
        }

        Rating rating = new Rating();
        rating.setProfileRecipient(profileRecipient);
        rating.setProfileSender(profileSender);
        rating.setRating(rateDto.getRate());

        ratingRepository.save(rating);
        log.info("Profile with id:{} was rated with rate:{}", profileRecipient.getId(), rateDto.getRate());
    }

    @Override
    public ProfileDto getProfile(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PROFILE_NOT_FOUND, id)));

        ProfileDto dto = mapper.toProfileDto(profile);

        log.info("Get profile with id:{}", id);
        return dto;
    }
}
