package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @ToString.Exclude
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_id")
    @ToString.Exclude
    private Advert advert;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "comment_create")
    private LocalDateTime createDate;

    @Column(name = "comment_update")
    private LocalDateTime updateDate;

    @Column(name = "comment_delete")
    private LocalDateTime deleteDate;

}
