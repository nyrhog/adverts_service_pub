package com.project.mapper;

import com.project.dto.AdvertDto;
import com.project.dto.UpdateAdvertDto;
import com.project.entity.Advert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface AdvertMapper {

    AdvertDto toAdvertDto(Advert advert);

    @Mapping(target = "advert.adName", source = "dto.adName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "advert.adPrice", source = "dto.adPrice", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "advert.description", source = "dto.description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdvert(@MappingTarget Advert advert, UpdateAdvertDto dto);

}
