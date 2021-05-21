package com.project.dao;

import com.project.entity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

//    Page<Advert> findAdvertByCategoriesInAndAdvertPremium_IsActiveTrueOrderByCreatedDesc(Collection<List<Category>> categories, Pageable pageable);

    //todo приделать сортировки и выборку
//    Page<Advert> findAdvertByCategoriesInOrderByCreatedDescAdvertPremium_IsActiveAsc(Collection<Category> categories, Pageable pageable);

}
