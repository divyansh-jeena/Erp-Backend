package com.example.erpsystem.purchase.controller;

import com.example.erpsystem.purchase.dto.PurchaseRequest;
import com.example.erpsystem.purchase.dto.PurchaseResponse;
import com.example.erpsystem.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/purchases")
@Validated
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PURCHASE')")
    public ResponseEntity<PurchaseResponse> createPurchase(
            @Valid @RequestBody PurchaseRequest request) {

        return ResponseEntity
                .status(201)
                .body(purchaseService.createPurchase(request));
    }

    @PreAuthorize("hasAuthority('UPDATE_PURCHASE')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PurchaseResponse> cancelPurchase(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                purchaseService.cancelPurchase(id)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_PURCHASE')")
    public ResponseEntity<PurchaseResponse> getPurchase(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                purchaseService.getPurchaseById(id)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PURCHASE')")
    public ResponseEntity<Page<PurchaseResponse>> getAllPurchases(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size
    ) {

        return ResponseEntity.ok(
                purchaseService.getAllPurchases(page, size)
        );
    }
}