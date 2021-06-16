package com.project.dao;

import com.project.entity.Profile;
import com.project.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating getRatingByProfileSenderAndProfileRecipient(Profile profile1, Profile profile2);

}
