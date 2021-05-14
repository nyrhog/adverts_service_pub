package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@EqualsAndHashCode
public class Comment {

    @EmbeddedId
    private CommentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("advertId")
    private Advert advert;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "comment_create")
    private LocalDateTime createDate;

    @Column(name = "comment_update")
    private LocalDateTime updateDate;

    @Column(name = "comment_delete")
    private LocalDateTime deleteDate;

    protected Comment() {
    }

    public Comment(Profile profile, Advert advert) {
        this.profile = profile;
        this.advert = advert;
        this.id = new CommentId(profile.getId(), advert.getId());
    }
}
