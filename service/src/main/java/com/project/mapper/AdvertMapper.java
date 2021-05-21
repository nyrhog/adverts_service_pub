package com.project.mapper;

import com.project.dto.AdvertDto;
import com.project.entity.Advert;
import org.mapstruct.Mapper;

@Mapper
public interface AdvertMapper {

    AdvertDto toAdvertDto(Advert advert);

}
