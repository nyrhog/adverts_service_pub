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
public class MessageId implements Serializable {

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "chat_id")
    private Long chatId;

    protected MessageId() {
    }

    public MessageId(Long profileId, Long chatId) {
        this.profileId = profileId;
        this.chatId = chatId;
    }
}
