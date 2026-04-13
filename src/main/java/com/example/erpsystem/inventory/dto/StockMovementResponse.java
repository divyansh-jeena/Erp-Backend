package com.example.erpsystem.inventory.dto;

import java.time.LocalDateTime;

public record StockMovementResponse(Long id,
                                    int quantityChange,
                                    String type,
                                    String reason,
                                    LocalDateTime createdAt) {
}
