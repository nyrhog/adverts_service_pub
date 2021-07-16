package com.project.entity;

import com.project.enums.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@EqualsAndHashCode(of = "id")
@ToString
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "update_date")
    private LocalDateTime updated;

    @Column(name = "close_date")
    private LocalDateTime closed;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_premium_id", referencedColumnName = "id")
    @ToString.Exclude
    private AdvertPremium advertPremium;

    @ManyToMany(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "adverts_categories",
            joinColumns = {@JoinColumn(name = "adverts_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    @ToString.Exclude
    private List<Category> categories;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "advert",
            cascade = {CascadeType.REMOVE},
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @ToString.Exclude
    private Profile profile;

    @OneToOne(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private BillingDetails billingDetails;

}
