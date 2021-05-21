package com.project.service;

import com.project.dao.ProfileRepository;
import com.project.dto.ProfileUpdateDto;
import com.project.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

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

}
