package com.project.dao;

import com.project.entity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertsRepository extends JpaRepository<Advert, Long> {
}
