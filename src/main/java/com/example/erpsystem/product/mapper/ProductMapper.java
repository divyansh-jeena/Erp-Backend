package com.example.erpsystem.product.mapper;

import com.example.erpsystem.product.dto.ProductRequest;
import com.example.erpsystem.product.dto.ProductResponse;
import com.example.erpsystem.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper{

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}