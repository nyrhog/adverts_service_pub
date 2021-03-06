package com.project.dao;

import com.project.entity.Advert;
import com.project.entity.Profile;
import com.project.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query(value = "SELECT a FROM Advert a " +
            "JOIN a.profile p " +
            "WHERE a.status = 'CLOSED' and p.id = ?1")
    Page<Advert> getAllClosedAdvertsWithProfileId(Long profileId, Pageable page);

    Optional<Advert> getAdvertByIdAndProfile_User_Username(Long id, String username);

    List<Advert> getAdvertsByProfileAndStatus(Profile profile, Status status);

}
