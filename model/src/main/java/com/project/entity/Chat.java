package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chats")
@EqualsAndHashCode(of = "id")
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Profile> profiles = new ArrayList<>();

    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime created;
}
