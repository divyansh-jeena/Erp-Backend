package com.example.erpsystem.purchase.repository;

import com.example.erpsystem.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository  extends JpaRepository<Purchase,Long> {
    Optional<Purchase> getPurchaseById(Long purchaseId);
}
