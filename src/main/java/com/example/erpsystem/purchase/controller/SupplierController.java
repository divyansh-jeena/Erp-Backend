package com.example.erpsystem.purchase.controller;

import com.example.erpsystem.purchase.entity.Supplier;
import com.example.erpsystem.purchase.service.SupplierService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;


    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SUPPLIER')")
    public ResponseEntity<Supplier> createSupplier(
            @RequestBody Supplier supplier) {

        return ResponseEntity
                .status(201)
                .body(supplierService.createSupplier(supplier));
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_SUPPLIER')")
    public ResponseEntity<Supplier> getSupplier(
            @PathVariable @Min(1) Long id) {

        return ResponseEntity.ok(
                supplierService.getSupplier(id)
        );
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_SUPPLIER')")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {

        return ResponseEntity.ok(
                supplierService.getAllSuppliers()
        );
    }
}