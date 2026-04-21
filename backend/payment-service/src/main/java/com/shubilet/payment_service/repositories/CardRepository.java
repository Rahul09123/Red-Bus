package com.shubilet.payment_service.repositories;

import com.shubilet.payment_service.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    // Müşterinin aktif kartlarını getirir
    List<Card> findByCustomerIdAndIsActiveTrue(Integer customerId);

    // GÜNCELLENEN KISIM:
    // "Bu müşteride", "Bu numaraya sahip", "Aktif" bir kart var mı?
    boolean existsByCardNoAndCustomerIdAndIsActiveTrue(String cardNo, Integer customerId);
}