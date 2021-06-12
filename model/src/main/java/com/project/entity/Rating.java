package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "SEQ_RATING", allocationSize = 1)
    private Long id;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
