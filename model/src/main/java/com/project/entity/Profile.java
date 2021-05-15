package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Accessors(chain = true)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    @SequenceGenerator(name = "profile_seq", sequenceName = "SEQ_PROFILE")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "rating")
//    @Size(min = 1, max = 5)
    private Double rating;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime created;

    @Column(name = "update_date")
    private LocalDateTime updated;

    @OneToMany
    @JoinColumn(name = "advert_id")
    private List<Advert> adverts = new ArrayList<>();

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "profiles_chats",
            joinColumns = {@JoinColumn(name = "profile_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    private List<Chat> chats = new ArrayList<>();
}
