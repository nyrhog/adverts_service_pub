package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "SEQ_CHAT", allocationSize = 1)
    private Long id;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY)
    private List<Profile> profiles = new ArrayList<>();

    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime created;
}
