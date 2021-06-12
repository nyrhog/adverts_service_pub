package com.project.service;

import com.project.dao.ProfileRepository;
import com.project.dao.RatingRepository;
import com.project.dto.ProfileDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.entity.Profile;
import com.project.entity.Rating;
import com.project.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final RatingRepository ratingRepository;
    private final ProfileMapper mapper;

    @Transactional
    @Override
    public void updateProfile(ProfileUpdateDto updateDto) {

        Profile profile = profileRepository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + updateDto.getId() + " not found"));

        String name = updateDto.getName();
        String phoneNumber = updateDto.getPhoneNumber();
        String surname = updateDto.getSurname();

        if (name != null) {
            profile.setName(name);
        }

        if (phoneNumber != null) {
            profile.setPhoneNumber(phoneNumber);
        }

        if (surname != null) {
            profile.setSurname(surname);
        }
    }

    @Transactional
    @Override
    public void rateProfile(RateDto rateDto){

        Profile profile = profileRepository.findById(rateDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + rateDto.getProfileId() + " not found"));

        Rating rating = new Rating();
        rating.setProfile(profile);
        rating.setRating(rateDto.getRate());

        ratingRepository.save(rating);
    }

    @Transactional
    @Override
    public ProfileDto getProfile(Long id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + id + " not found"));

        ProfileDto dto = mapper.toProfileDto(profile);

        return dto;
    }



}
