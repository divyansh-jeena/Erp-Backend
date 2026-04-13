package com.example.erpsystem.purchase.dto;

import java.math.BigDecimal;

public record PurchaseItemResponse(
        Long productId,
        int quantity,
        BigDecimal price,
        BigDecimal total
) {}