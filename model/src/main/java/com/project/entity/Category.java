package com.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "mainCategory", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Category> categories;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category mainCategory;

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private List<Advert> adverts;

}
