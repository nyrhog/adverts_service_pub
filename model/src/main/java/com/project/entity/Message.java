package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@EqualsAndHashCode
public class Message {

    @EmbeddedId
    private MessageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatId")
    private Chat chat;

    @Column(name = "message_text")
    private String text;

    @Column(name = "message_write_time")
    private LocalDateTime writeTime;

    protected Message() {
    }

    public Message(Profile profile, Chat chat) {
        this.profile = profile;
        this.chat = chat;
        this.id = new MessageId(profile.getId(), chat.getId());
    }


}
