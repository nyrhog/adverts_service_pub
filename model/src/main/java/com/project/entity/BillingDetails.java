package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paymentCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_id", referencedColumnName = "id")
    @ToString.Exclude
    private Advert advert;

    @Column
    private Double price;

    @Column
    private Integer days;

}
