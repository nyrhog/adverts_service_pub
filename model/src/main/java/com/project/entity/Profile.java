package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Accessors(chain = true)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    @SequenceGenerator(name = "profile_seq", sequenceName = "SEQ_PROFILE", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @org.hibernate.annotations.Formula(
           "(select avg(r.rating) from ratings r where r.profile_id = id)"
    )
    private Double ratingValue;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime created;

    @Column(name = "update_date")
    private LocalDateTime updated;

    @OneToMany
    @JoinColumn(name = "profile_id")
    private List<Advert> adverts = new ArrayList<>();

    @OneToMany(
            mappedBy = "profile",
            cascade = {CascadeType.REMOVE, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "profiles_chats",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    private List<Chat> chats = new ArrayList<>();

    @OneToOne(mappedBy = "profile")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) && Objects.equals(name, profile.name) && Objects.equals(surname, profile.surname) && Objects.equals(phoneNumber, profile.phoneNumber) && Objects.equals(ratingValue, profile.ratingValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phoneNumber, ratingValue);
    }
}
