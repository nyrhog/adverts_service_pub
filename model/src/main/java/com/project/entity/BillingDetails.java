package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_details_seq")
    @SequenceGenerator(name = "billing_details_seq", sequenceName = "SEQ_BILLING_DETAILS", allocationSize = 1)
    private Long id;

    @Column
    private String paymentCount;

    @OneToOne
    @JoinColumn(name = "advert_id", referencedColumnName = "id")
    private Advert advert;

    @Column
    private Double price;

    @Column
    private Integer days;
}
