package com.project.mapper;

import com.project.dto.CategoryDto;
import com.project.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);
    CategoryDto toCategoryDto(Category category);

}
