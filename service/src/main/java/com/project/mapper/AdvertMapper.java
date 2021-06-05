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

    @Mapping(target = "", source  = "", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "advert.adName", source = "dto.adName")
    @Mapping(target = "advert.adPrice", source = "dto.adPrice")
    @Mapping(target = "advert.description", source = "dto.description")
    void updateAdvert(@MappingTarget Advert advert, UpdateAdvertDto dto);

}
