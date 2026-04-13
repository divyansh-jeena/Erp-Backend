package com.example.erpsystem.sales.mapper;

import com.example.erpsystem.sales.dto.OrderItemResponse;
import com.example.erpsystem.sales.dto.OrderResponse;
import com.example.erpsystem.sales.entity.Order;
import com.example.erpsystem.sales.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {

        if (order == null) {
            return null;
        }

        List<OrderItemResponse> items = order.getItems() == null
                ? List.of()
                : order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        String status = order.getStatus() != null
                ? order.getStatus().name()
                : null;

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                status,
                items
        );
    }

    public OrderItemResponse toItemResponse(OrderItem item) {

        if (item == null) {
            return null;
        }

        BigDecimal price = item.getPrice() != null
                ? item.getPrice()
                : BigDecimal.ZERO;

        BigDecimal total = price.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(
                item.getProductId(),
                item.getQuantity(),
                price,
                total
        );
    }
}