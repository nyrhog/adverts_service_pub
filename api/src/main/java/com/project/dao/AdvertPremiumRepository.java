package com.project.dao;

import com.project.entity.AdvertPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvertPremiumRepository extends JpaRepository<AdvertPremium, Long> {

    List<AdvertPremium> getAllByPremEndLessThanAndIsActiveTrue(LocalDateTime time);

}
