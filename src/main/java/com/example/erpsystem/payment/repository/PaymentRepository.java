package com.example.erpsystem.payment.repository;

import com.example.erpsystem.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface  PaymentRepository   extends JpaRepository<Payment,Long> {
}
