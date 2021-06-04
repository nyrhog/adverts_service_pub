package com.project.service;

import com.project.dto.ProfileUpdateDto;
import com.project.dto.RateDto;

public interface IProfileService {

    void updateProfile(ProfileUpdateDto updateDto);
    void rateProfile(RateDto rateDto);


}
