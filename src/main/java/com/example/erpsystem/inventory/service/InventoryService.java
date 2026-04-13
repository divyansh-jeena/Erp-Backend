package com.example.erpsystem.inventory.service;

import com.example.erpsystem.inventory.dto.InventoryResponse;

import com.example.erpsystem.inventory.dto.StockMovementResponse;
import com.example.erpsystem.inventory.entity.Inventory;
import com.example.erpsystem.inventory.entity.StockMovement;
import com.example.erpsystem.inventory.entity.enums.MovementType;
import com.example.erpsystem.shared.exception.ResourceNotFoundException;
import com.example.erpsystem.inventory.mapper.StockMovementMapper;
import com.example.erpsystem.inventory.repository.InventoryRepository;
import com.example.erpsystem.inventory.repository.StockMovementRepository;

import com.example.erpsystem.product.repository.ProductRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
 private final ProductRepository productRepository;
    @Transactional
    public InventoryResponse addStock(Long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found for product id: " + productId));

        inventory.setQuantity(inventory.getQuantity() + quantity);

        StockMovement movement = StockMovement.builder()
                .product(inventory.getProduct())
                .quantityChange(quantity)
                .type(MovementType.ADD)
                .reason("Stock added")
                .createdAt(LocalDateTime.now())
                .build();

        stockMovementRepository.save(movement);

        return mapToResponse(inventory);
    }

    @Transactional
    public InventoryResponse reduceStock(Long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found for product id: " + productId));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);

        StockMovement movement = StockMovement.builder()
                .product(inventory.getProduct())
                .quantityChange(-quantity)
                .type(MovementType.REDUCE)
                .reason("Stock reduced")
                .createdAt(LocalDateTime.now())
                .build();

        stockMovementRepository.save(movement);

        return mapToResponse(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .productId(inventory.getProduct().getId())
                .quantity(inventory.getQuantity())
                .build();
    }

    public Page<StockMovementResponse> getStockHistory(
            Long productId,
            @Min(0) int page,
            @Min(1) @Max(50) int size
    ) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Pageable pageable = PageRequest.of(page, size);

        return stockMovementRepository.findByProductId(productId, pageable)
                .map(stockMovementMapper::toStockMovementResponse);
    }
}