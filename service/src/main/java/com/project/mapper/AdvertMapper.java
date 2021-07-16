package com.project.mapper;

import com.project.dto.AdvertDto;
import com.project.dto.UpdateAdvertDto;
import com.project.entity.Advert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdvertMapper {

    @Mapping(target = "creatorProfileId", source = "advert.profile.id")
    AdvertDto toAdvertDto(Advert advert);

    @Mapping(target = "advert.adName", source = "dto.adName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "advert.adPrice", source = "dto.adPrice", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "advert.description", source = "dto.description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdvert(@MappingTarget Advert advert, UpdateAdvertDto dto);
}
