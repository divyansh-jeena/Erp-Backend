package com.example.erpsystem.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseResponse(
        Long purchaseId,
        Long supplierId,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<PurchaseItemResponse> items
) {

}