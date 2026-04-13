package com.example.erpsystem.inventory.mapper;

import com.example.erpsystem.inventory.dto.StockMovementResponse;
import com.example.erpsystem.inventory.entity.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovementResponse toStockMovementResponse(StockMovement stockMovement) {

        if (stockMovement == null) {
            return null;
        }

        String type = stockMovement.getType() != null
                ? stockMovement.getType().name()
                : null;

        return new StockMovementResponse(
                stockMovement.getId(),
                stockMovement.getQuantityChange(),
                type,
                stockMovement.getReason(),
                stockMovement.getCreatedAt()
        );
    }
}