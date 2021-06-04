package com.project.dao;

import com.project.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query(value = "SELECT a FROM Advert a " +
            "JOIN a.profile p " +
            "WHERE a.status = 'CLOSED' and p.id = ?1")
    Page<Advert> getAllClosedAdvertsWithProfileId(Long profileId, Pageable page);

}
