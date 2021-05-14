package com.project.dao;

import com.project.entity.AdvertPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertPremiumRepository extends JpaRepository<AdvertPremium, Long> {
}