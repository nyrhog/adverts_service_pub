package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adverts")
@Getter
@Setter
@Accessors(chain = true)
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adverts_seq")
    @SequenceGenerator(name = "adverts_seq", sequenceName = "SEQ_ADVERTS", allocationSize = 1)
    private Long id;

    @Column(name = "ad_name", nullable = false)
    private String adName;

    @Column(name = "ad_price", nullable = false)
    private Double adPrice;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "create_date")
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime created;

    @Column(name = "update_name")
    private LocalDateTime updated;

    @Column(name = "close_date")
    private LocalDateTime closed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "advert_premium_id", referencedColumnName = "id")
    private AdvertPremium advertPremium;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "adverts_categories",
            joinColumns = {@JoinColumn(name = "adverts_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Category> categories;

    @OneToMany(
            mappedBy = "advert",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();
}
