package com.example.erpsystem.payment.service;

import com.example.erpsystem.shared.exception.ResourceNotFoundException;
import com.example.erpsystem.payment.entity.Payment;
import com.example.erpsystem.payment.entity.enums.PaymentStatus;
import com.example.erpsystem.payment.repository.PaymentRepository;
import com.example.erpsystem.sales.entity.Order;
import com.example.erpsystem.sales.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentStatus processPayment(Long orderId, BigDecimal amount) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));


        PaymentStatus status = simulatePayment();

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .status(status)
                .build();

        paymentRepository.save(payment);

        return status;
    }

    private PaymentStatus simulatePayment() {
        return new Random().nextBoolean()
                ? PaymentStatus.SUCCESS
                : PaymentStatus.FAILED;
    }
}