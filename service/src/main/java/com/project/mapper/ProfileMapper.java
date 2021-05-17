package com.project.mapper;

import com.project.dto.ProfileUpdateDto;
import com.project.entity.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper {

    ProfileUpdateDto toProfileDto(Profile profile);

    Profile toProfile(ProfileUpdateDto profileDto);

}
