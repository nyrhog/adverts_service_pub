package com.project.service;

import com.project.dao.ProfileRepository;
import com.project.dto.CreateAdvertDto;
import com.project.entity.Advert;
import com.project.entity.AdvertPremium;
import com.project.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AdvertService implements IAdvertService {

    private ProfileRepository profileRepository;

    @Autowired
    public AdvertService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    @Override
    public void createAdvert(CreateAdvertDto advertDto) {

        Profile profile = profileRepository.findById(advertDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException("Profile with id: " + advertDto.getProfileId() + " not found"));

        AdvertPremium advertPremium = new AdvertPremium()
                .setIsActive(false);

        Advert advert = new Advert()
                .setAdName(advertDto.getAdName())
                .setAdPrice(advertDto.getAdPrice())
                .setDescription(advertDto.getDescription())
                .setAdvertPremium(advertPremium);

        profile.getAdverts().add(advert);

        profileRepository.saveAndFlush(profile);
    }

    @Override
    public void updateAdvert() {

    }

    @Override
    public void deleteAdvert() {

    }
}
