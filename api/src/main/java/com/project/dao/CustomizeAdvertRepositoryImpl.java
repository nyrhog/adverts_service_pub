package com.project.dao;

import com.project.entity.*;
import com.project.enums.Status;
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
    public Page<Advert> findAllByCategoriesIn(List<String> categories, String search, Pageable page) {

        Long singleResult = getCategoryJoinCount(categories);

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

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(advertCategoryJoin.get(Category_.CATEGORY_NAME).in(categories));
        predicates.add(cb.equal(root.get(Advert_.STATUS), Status.ACTIVE));


        if(search != null && !search.trim().isEmpty()){
            predicates.add(cb.like(root.get(Advert_.AD_NAME), String.format("%%%s%%", search)));
        }

        cq.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(orders);


        return createPagedResult(cq, page, singleResult);
    }

    public Page<Advert> findAllClosedByProfileId(Long id, Pageable page) {

        Long singleResult = getCountClosedAdvertsCount(id);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advert> cq = cb.createQuery(Advert.class);
        Root<Advert> root = cq.from(Advert.class);
        Join<Advert, Profile> advertProfileJoin = root.join(Advert_.PROFILE);

        List<Order> orders = new ArrayList<>();
        orders.add(cb.desc(root.get(Advert_.CLOSED)));

        cq.select(root)
                .where(
                        cb.and
                                (cb.equal(advertProfileJoin.get(Profile_.ID), id),
                                 cb.equal(root.get(Advert_.STATUS), Status.CLOSED)
                                )
                )
                .orderBy(orders);


        return createPagedResult(cq, page, singleResult);
    }

    private Page<Advert> createPagedResult(CriteriaQuery<Advert> cq, Pageable page, Long singleResult) {
        TypedQuery<Advert> query = entityManager.createQuery(cq);

        query.setFirstResult((int) page.getOffset());
        query.setMaxResults(page.getPageSize());

        List<Advert> resultList = query.getResultList();

        Page<Advert> result = new PageImpl<>(resultList, page, singleResult);

        if (page.getPageNumber() >= result.getTotalPages()) {
            throw new OutOfAvailablePagesException("Requested page number greater then available with page number: " + (page.getPageNumber() + 1));
        }

        return result;
    }

    private Long getCategoryJoinCount(List<String> categories) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Advert> root = cq.from(Advert.class);
        Join<Object, Object> join = root.join(Advert_.CATEGORIES);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(join.get(Category_.CATEGORY_NAME).in(categories));
        predicates.add(cb.equal(root.get(Advert_.STATUS), Status.ACTIVE));

        cq.select(cb.count(root)).
                where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getSingleResult();
    }

    private Long getCountClosedAdvertsCount(Long profileId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Advert> root = cq.from(Advert.class);
        Join<Advert, Profile> join = root.join(Advert_.PROFILE);

        cq.select(cb.count(root)).where(
                cb.and
                        (cb.equal(join.get(Profile_.ID), profileId),
                         cb.equal(root.get(Advert_.STATUS), Status.CLOSED)
                        )
        );

        return entityManager.createQuery(cq).getSingleResult();
    }

}
