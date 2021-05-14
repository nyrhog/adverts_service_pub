package com.project.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "premium_adverts_details")
public class AdvertPremium {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adverts_premium_seq")
    @SequenceGenerator(name = "adverts_premium_seq", sequenceName = "SEQ_ADVERTS_PREMIUM")
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "premium_started")
    private LocalDateTime premStarted;

    @Column(name = "premium_end")
    private LocalDateTime premEnd;

    @OneToOne(
            mappedBy = "advertPremium",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Advert advert;
}
