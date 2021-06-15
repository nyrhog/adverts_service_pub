package com.project.dao;

import com.project.entity.Advert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query(value = "SELECT a FROM Advert a " +
            "JOIN a.profile p " +
            "WHERE a.status = 'CLOSED' and p.id = ?1")
    Page<Advert> getAllClosedAdvertsWithProfileId(Long profileId, Pageable page);

    Optional<Advert> getAdvertByIdAndProfile_User_Username(Long id, String username);

}
