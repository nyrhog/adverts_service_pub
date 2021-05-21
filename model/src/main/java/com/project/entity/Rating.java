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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
