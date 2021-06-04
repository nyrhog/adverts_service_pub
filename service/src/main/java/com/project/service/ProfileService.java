package com.project.service;

import com.project.dao.AdvertRepository;
import com.project.dao.ProfileRepository;
import com.project.dao.RatingRepository;
import com.project.dto.AdvertDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.entity.Profile;
import com.project.entity.Rating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    @Override
    public void updateProfile(ProfileUpdateDto updateDto) {

        Profile profile = profileRepository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + updateDto.getId() + " not found"));

        String name = updateDto.getName();
        String surname = updateDto.getSurname();
        String phone = updateDto.getPhoneNumber();

        if (name != null) {
            profile.setName(name);
            log.info("Profile with id: {} was updated with name {}", updateDto.getId(), name);
        }

        if (surname != null) {
            profile.setSurname(surname);
            log.info("Profile with id: {} was updated with surname {}", updateDto.getId(), surname);
        }

        if (phone != null) {
            profile.setPhoneNumber(phone);
            log.info("Profile with id: {} was updated with phone {}", updateDto.getId(), phone);
        }
    }

    @Transactional
    @Override
    public void rateProfile(RateDto rateDto){

        Profile profile = profileRepository.findById(rateDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + rateDto.getProfileId() + " not found"));

        Rating rating = new Rating();
        rating.setProfile(profile);
        rating.setRating(rating.getRating());

        ratingRepository.save(rating);
    }



}
