package com.project.dao;

import com.project.entity.Advert;
import com.project.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdvertsRepository extends JpaRepository<Advert, Long> {

//    Page<Advert> findAdvertByCategoriesInAndAdvertPremium_IsActiveTrueOrderByCreatedDesc(Collection<List<Category>> categories, Pageable pageable);

    //todo приделать сортировки и выборку
    Page<Advert> findAdvertByCategoriesInOrderByCreatedDescAdvertPremium_IsActiveAsc(Collection<List<Category>> categories, Pageable pageable);

}
