package com.ellen.creditcard.onboarding.repository;

import com.ellen.creditcard.onboarding.entity.CreditCardApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ellen
 * @date 2026/03/18
 * credit card application repository
 */
@Repository
public interface CreditCardApplicationRepository extends JpaRepository<CreditCardApplication, Long> {
}
