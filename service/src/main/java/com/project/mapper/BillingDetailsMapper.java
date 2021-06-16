package com.project.mapper;

import com.project.dto.BillingDetailsDto;
import com.project.entity.BillingDetails;
import org.mapstruct.Mapper;

@Mapper
public interface BillingDetailsMapper {

    BillingDetailsDto toBillingDetailsDto(BillingDetails details);

}
