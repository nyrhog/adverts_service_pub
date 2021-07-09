package com.project.service;

import com.project.dto.ProfileDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;
import com.project.entity.Profile;
import com.project.entity.Rating;

public interface IProfileService {

    Profile updateProfile(ProfileUpdateDto updateDto);
    Rating rateProfile(RateDto rateDto);
    ProfileDto getProfile(Long id);


}
