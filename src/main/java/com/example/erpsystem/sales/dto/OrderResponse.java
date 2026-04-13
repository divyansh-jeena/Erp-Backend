package com.example.erpsystem.sales.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(

        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        String status,
        List<OrderItemResponse> items

) {}