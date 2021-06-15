package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "premium_adverts_details")
@Getter
@Setter
@Accessors(chain = true)
public class AdvertPremium {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adverts_premium_seq")
    @SequenceGenerator(name = "adverts_premium_seq", sequenceName = "SEQ_ADVERTS_PREMIUM", allocationSize = 1)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "premium_started")
    private LocalDateTime premStarted;

    @Column(name = "premium_end")
    private LocalDateTime premEnd;

}
