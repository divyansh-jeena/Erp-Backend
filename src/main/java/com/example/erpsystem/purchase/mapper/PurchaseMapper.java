package com.example.erpsystem.purchase.mapper;
import com.example.erpsystem.purchase.dto.PurchaseItemResponse;
import com.example.erpsystem.purchase.dto.PurchaseResponse;
import com.example.erpsystem.purchase.entity.Purchase;
import com.example.erpsystem.purchase.entity.PurchaseItem;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class PurchaseMapper {

    public  PurchaseResponse toResponse(Purchase purchase) {

        List<PurchaseItemResponse> itemResponses =purchase.getItems()
                .stream()
                .map(this::mapItem)
                .toList();

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getSupplier().getId(),
                purchase.getStatus().name(),
                purchase.getTotalAmount(),
                purchase.getCreatedAt(),
                itemResponses
        );
    }

    private PurchaseItemResponse mapItem(PurchaseItem item) {

        BigDecimal total = item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new PurchaseItemResponse(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice(),
                total
        );
    }
}