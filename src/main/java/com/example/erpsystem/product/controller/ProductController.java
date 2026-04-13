package com.example.erpsystem.product.controller;

import com.example.erpsystem.product.dto.ProductRequest;
import com.example.erpsystem.product.dto.ProductResponse;
import com.example.erpsystem.product.service.ProductService;
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
@Validated
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity
                .status(201)
                .body(productService.createProduct(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    public ResponseEntity<Page<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        return ResponseEntity.ok(
                productService.getAllProducts(page, size)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    public ResponseEntity<ProductResponse> get(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                productService.getProduct(id)
        );
    }


    @PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(id, request)
        );
    }
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}