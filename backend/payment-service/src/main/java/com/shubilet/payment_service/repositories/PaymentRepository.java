package com.shubilet.payment_service.repositories;

import com.shubilet.payment_service.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**

    Repository interface for Payment entities. Provides CRUD operations
    and helper methods to query payments related to a customer.

 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Returns all payments that belong to the given customer.
     *
     * @param customerId the identifier of the customer
     * @return list of payments for that customer
    
    List<Payment> findByCustomerId(String customerId); */
    // Yine: Payment modelinde customerId Long ise burayı Long yap.
}
