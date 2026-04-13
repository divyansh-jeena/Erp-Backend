package com.example.erpsystem.purchase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseItemRequest (
        @NotNull
        Long productId,

        @Min(1)
        int quantity,

        @NotNull
        BigDecimal price){
}
