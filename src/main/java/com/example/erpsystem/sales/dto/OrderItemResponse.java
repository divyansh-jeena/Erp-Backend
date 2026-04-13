package com.example.erpsystem.sales.dto;

import java.math.BigDecimal;

public record OrderItemResponse(

        Long productId,
        int quantity,
        BigDecimal price,
        BigDecimal total

) {}