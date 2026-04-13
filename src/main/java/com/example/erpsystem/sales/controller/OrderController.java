package com.example.erpsystem.sales.controller;

import com.example.erpsystem.sales.dto.OrderRequest;
import com.example.erpsystem.sales.dto.OrderResponse;
import com.example.erpsystem.sales.service.OrderService;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        return ResponseEntity
                .status(201)
                .body(orderService.createOrder(request));
    }

    @PreAuthorize("hasAuthority('UPDATE_ORDER')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                orderService.cancelOrder(id)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        return ResponseEntity.ok(
                orderService.getAllOrders(page, size)
        );
    }
}