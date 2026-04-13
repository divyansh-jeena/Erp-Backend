package com.example.erpsystem.product.service;

import com.example.erpsystem.inventory.entity.Inventory;
import com.example.erpsystem.inventory.repository.InventoryRepository;
import com.example.erpsystem.product.dto.ProductRequest;
import com.example.erpsystem.product.dto.ProductResponse;
import com.example.erpsystem.product.entity.Product;
import com.example.erpsystem.shared.exception.ResourceNotFoundException;
import com.example.erpsystem.product.mapper.ProductMapper;
import com.example.erpsystem.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        validateProduct(request);

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);


        Inventory inventory = Inventory.builder()
                .product(savedProduct)
                .quantity(0)
                .build();

        inventoryRepository.save(inventory);

        return productMapper.toResponse(savedProduct);
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        validateProduct(request);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());


        return productMapper.toResponse(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }

    private void validateProduct(ProductRequest request) {

        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}