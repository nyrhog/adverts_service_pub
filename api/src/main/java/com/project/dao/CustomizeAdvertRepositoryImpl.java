package com.project.dao;

import com.project.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomizeAdvertRepositoryImpl implements CustomizedAdvertRepository<Advert> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Advert> findAllByCategoriesIn(List<String> categories, Pageable page) {

        Long singleResult = getCount();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advert> cq = cb.createQuery(Advert.class);
        Root<Advert> root = cq.from(Advert.class);
        Join<Advert, AdvertPremium> advertPremiumJoin = root.join(Advert_.ADVERT_PREMIUM);
        Join<Advert, Category> advertCategoryJoin = root.join(Advert_.CATEGORIES);
        Join<Advert, Profile> advertProfileJoin = root.join(Advert_.PROFILE);

        List<Order> orders = new ArrayList<>();
        orders.add(cb.desc(advertPremiumJoin.get(AdvertPremium_.PREM_STARTED)));
        orders.add(cb.desc(advertProfileJoin.get(Profile_.RATING_VALUE)));
        orders.add(cb.desc(root.get(Advert_.CREATED)));

        cq.select(root)
                .where(cb.and(advertCategoryJoin.get(Category_.CATEGORY_NAME).in(categories),
                        cb.equal(root.get(Advert_.STATUS), Status.ACTIVE)))
                .orderBy(orders);

        TypedQuery<Advert> query = entityManager.createQuery(cq);

        query.setFirstResult((int) page.getOffset());
        query.setMaxResults(page.getPageSize());

        List<Advert> resultList = query.getResultList();

        Page<Advert> result = new PageImpl<>(resultList, page, singleResult);

        if(page.getPageNumber() >= result.getTotalPages()){
            throw new OutOfAvailablePagesException("Requested page number greater then available with page number: " + page.getPageNumber());
        }

        return result;
    }

    private Long getCount(){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Advert> root = cq.from(Advert.class);
        root.join(Advert_.CATEGORIES);

        cq.select(cb.count(root));

        return entityManager.createQuery(cq).getSingleResult();
    }

}
