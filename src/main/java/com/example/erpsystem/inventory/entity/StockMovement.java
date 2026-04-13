package com.example.erpsystem.inventory.entity;

import com.example.erpsystem.inventory.entity.enums.MovementType;
import com.example.erpsystem.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    private int quantityChange;


    @Enumerated(EnumType.STRING)
    private MovementType type;


    private String reason;

    private LocalDateTime createdAt;
}