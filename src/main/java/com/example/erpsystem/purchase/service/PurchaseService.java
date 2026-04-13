package com.example.erpsystem.purchase.service;
import com.example.erpsystem.shared.exception.ResourceNotFoundException;
import com.example.erpsystem.inventory.service.InventoryService;
import com.example.erpsystem.product.entity.Product;
import com.example.erpsystem.product.repository.ProductRepository;
import com.example.erpsystem.purchase.dto.PurchaseItemRequest;
import com.example.erpsystem.purchase.dto.PurchaseRequest;
import com.example.erpsystem.purchase.dto.PurchaseResponse;
import com.example.erpsystem.purchase.entity.Purchase;
import com.example.erpsystem.purchase.entity.PurchaseItem;
import com.example.erpsystem.purchase.entity.Supplier;
import com.example.erpsystem.purchase.entity.enums.PurchaseStatus;
import com.example.erpsystem.purchase.mapper.PurchaseMapper;
import com.example.erpsystem.purchase.repository.PurchaseRepository;
import com.example.erpsystem.purchase.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PurchaseService {

 private final ProductRepository productRepository;
 private final PurchaseRepository purchaseRepository;
 private final InventoryService inventoryService;
 private final SupplierRepository supplierRepository;
 private final PurchaseMapper purchaseMapper;


    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {


        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }


        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found: " + request.supplierId()));


        Map<Long, PurchaseItemRequest> mergedItems = new HashMap<>();

        for (PurchaseItemRequest item : request.items()) {

            if (item.quantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for product: " + item.productId());
            }

            if (item.price() == null || item.price().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Invalid price for product: " + item.productId());
            }

            mergedItems.merge(
                    item.productId(),
                    item,
                    (existing, incoming) -> new PurchaseItemRequest(
                            existing.productId(),
                            existing.quantity() + incoming.quantity(),
                            incoming.price()
                    )
            );
        }


        List<Long> productIds = new ArrayList<>(mergedItems.keySet());
        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        Map<Long, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getId(), p);
        }


        Purchase purchase = Purchase.builder()
                .supplier(supplier)
                .status(PurchaseStatus.CREATED)
                .build();

        List<PurchaseItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        for (PurchaseItemRequest req : mergedItems.values()) {

            Product product = productMap.get(req.productId());

            BigDecimal itemTotal = req.price()
                    .multiply(BigDecimal.valueOf(req.quantity()));

            total = total.add(itemTotal);

            PurchaseItem item = PurchaseItem.builder()
                    .product(product)
                    .quantity(req.quantity())
                    .price(req.price())
                    .purchase(purchase)
                    .build();

            items.add(item);
        }

        purchase.setItems(items);
        purchase.setTotalAmount(total);

        Purchase savedPurchase = purchaseRepository.save(purchase);


        mergedItems.forEach((productId, item) ->
                inventoryService.addStock(productId, item.quantity())
        );

        savedPurchase.setStatus(PurchaseStatus.COMPLETED);
        return purchaseMapper.toResponse(savedPurchase);
    }
    @Transactional
    public PurchaseResponse cancelPurchase(Long purchaseId) {

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Purchase not found with id: " + purchaseId));

        if (purchase.getStatus() != PurchaseStatus.COMPLETED) {
            throw new IllegalStateException("Only completed purchases can be cancelled");
        }

        for (PurchaseItem item : purchase.getItems()) {
            inventoryService.reduceStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }

        purchase.setStatus(PurchaseStatus.CANCELLED);

        return purchaseMapper.toResponse(purchase);
    }

    public PurchaseResponse getPurchaseById(Long id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Purchase not found with id: " + id));

        return purchaseMapper.toResponse(purchase);
    }

    public Page<PurchaseResponse> getAllPurchases(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return purchaseRepository.findAll(pageable)
                .map(purchaseMapper::toResponse);
    }
}
