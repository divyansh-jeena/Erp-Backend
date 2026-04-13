package com.example.erpsystem.product.repository;

import com.example.erpsystem.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findById(Long id);

}