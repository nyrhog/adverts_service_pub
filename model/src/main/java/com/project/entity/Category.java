package com.project.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "SEQ_CATEGORY", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String categoryName;

    @OneToMany(mappedBy = "mainCategory")
    private List<Category> categories;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category mainCategory;

    @ManyToMany(mappedBy = "categories")
    private List<Advert> adverts;

}
