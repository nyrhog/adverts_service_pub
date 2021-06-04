package com.project.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomizedAdvertRepository<T> {
    Page<T> findAllByCategoriesIn(List<String> categories, Pageable pageable);
    Page<T> findAllClosedByProfileId(Long id, Pageable page);
}
