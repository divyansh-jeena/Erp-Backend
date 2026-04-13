package com.example.erpsystem.inventory.dto;

import jakarta.validation.constraints.Min;

public record StockUpdateRequest(

        @Min(value = 1, message = "Quantity must be greater than 0")
        int quantity

) {}