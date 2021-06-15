package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "SEQ_CATEGORY", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "mainCategory")
    private List<Category> categories;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category mainCategory;

    @ManyToMany(mappedBy = "categories")
    private List<Advert> adverts;

}
