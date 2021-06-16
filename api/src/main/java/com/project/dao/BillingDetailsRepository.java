package com.project.dao;

import com.project.entity.BillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingDetailsRepository extends JpaRepository<BillingDetails, Long> {

    BillingDetails findBillingDetailsByPaymentCount(String count);

}
