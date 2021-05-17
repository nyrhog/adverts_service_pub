package com.project.service;


import com.project.dto.CreateAdvertDto;

public interface IAdvertService {

    void createAdvert(CreateAdvertDto advertDto);
    void updateAdvert();
    void deleteAdvert();

}
