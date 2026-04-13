package com.example.erpsystem.inventory.controller;

import com.example.erpsystem.inventory.dto.InventoryResponse;
import com.example.erpsystem.inventory.dto.StockMovementResponse;
import com.example.erpsystem.inventory.dto.StockUpdateRequest;
import com.example.erpsystem.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;
    @PreAuthorize("hasAuthority('UPDATE_INVENTORY')")
    @PatchMapping("/{productId}/add-stock")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable @Min(1) Long productId,
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                inventoryService.addStock(productId, request.quantity())
        );
    }
    @PreAuthorize("hasAuthority('UPDATE_INVENTORY')")
    @PatchMapping("/{productId}/reduce-stock")
    public ResponseEntity<InventoryResponse> reduceStock(
            @PathVariable @Min(1) Long productId,
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                inventoryService.reduceStock(productId, request.quantity())
        );
    }
    @PreAuthorize("hasAuthority('VIEW_INVENTORY')")
    @GetMapping("/{productId}/history")
    public ResponseEntity<Page<StockMovementResponse>> getStockHistory(
            @PathVariable @Min(1) Long productId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        return ResponseEntity.ok(
                inventoryService.getStockHistory(productId, page, size)
        );
    }
}