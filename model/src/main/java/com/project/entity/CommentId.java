package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class CommentId implements Serializable {

    @Column(name = "advert_id")
    private Long advertId;

    @Column(name = "profile_id")
    private Long profileId;

    public CommentId(Long advertId, Long profileId) {
        this.advertId = advertId;
        this.profileId = profileId;
    }

    protected CommentId() {
    }
}
