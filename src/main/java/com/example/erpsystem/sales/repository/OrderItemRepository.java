package com.example.erpsystem.sales.repository;

import com.example.erpsystem.sales.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository  extends JpaRepository<OrderItem,Long> {
}
