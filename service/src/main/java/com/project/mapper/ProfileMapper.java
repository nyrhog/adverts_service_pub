package com.project.mapper;

import com.project.dto.ProfileDto;
import com.project.dto.ProfileUpdateDto;
import com.project.dto.UpdateAdvertDto;
import com.project.entity.Advert;
import com.project.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface ProfileMapper {

    ProfileDto toProfileDto(Profile profile);

    Profile toProfile(ProfileUpdateDto profileDto);

    @Mapping(target = "profile.name", source = "dto.name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profile.surname", source = "dto.surname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profile.phoneNumber", source = "dto.phoneNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdvert(@MappingTarget Profile profile, ProfileUpdateDto dto);

}
