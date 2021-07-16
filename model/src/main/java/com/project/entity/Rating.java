package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id_recipient")
    @ToString.Exclude
    private Profile profileRecipient;

    @ManyToOne
    @JoinColumn(name = "profile_id_sender")
    private Profile profileSender;
}
