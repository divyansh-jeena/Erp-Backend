package com.example.erpsystem.sales.service;

import com.example.erpsystem.payment.entity.enums.PaymentStatus;
import com.example.erpsystem.payment.service.PaymentService;
import com.example.erpsystem.product.entity.Product;
import com.example.erpsystem.shared.exception.IllegalStatusException;
import com.example.erpsystem.shared.exception.ResourceNotFoundException;
import com.example.erpsystem.product.repository.ProductRepository;
import com.example.erpsystem.inventory.service.InventoryService;
import com.example.erpsystem.sales.dto.OrderItemRequest;
import com.example.erpsystem.sales.dto.OrderRequest;
import com.example.erpsystem.sales.dto.OrderResponse;
import com.example.erpsystem.sales.entity.Order;
import com.example.erpsystem.sales.entity.OrderItem;
import com.example.erpsystem.sales.entity.OrderStatus;
import com.example.erpsystem.sales.mapper.OrderMapper;
import com.example.erpsystem.sales.repository.OrderRepository;
import com.example.erpsystem.shared.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SecurityUtil securityUtil;
    private final PaymentService paymentService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {


        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Long userId = securityUtil.getCurrentUserId();


        Map<Long, Integer> mergedItems = new HashMap<>();
        for (OrderItemRequest item : request.items()) {
            if (item.quantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for product: " + item.productId());
            }
            mergedItems.merge(item.productId(), item.quantity(), Integer::sum);
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


        mergedItems.forEach(inventoryService::reduceStock);


        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : mergedItems.entrySet()) {

            Product product = productMap.get(entry.getKey());

            BigDecimal price = product.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(entry.getValue()));

            total = total.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .quantity(entry.getValue())
                    .price(price)
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);


        Order savedOrder = orderRepository.save(order);


        PaymentStatus paymentStatus =
                paymentService.processPayment(savedOrder.getId(), total);


        if (paymentStatus == PaymentStatus.SUCCESS) {

            savedOrder.setStatus(OrderStatus.COMPLETED);

        } else {


            mergedItems.forEach(inventoryService::addStock);

            savedOrder.setStatus(OrderStatus.FAILED);
        }


        return orderMapper.toResponse(savedOrder);
    }
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw   new ResourceNotFoundException("Only completed orders can be cancelled");
        }

        for (OrderItem item : order.getItems()) {
            inventoryService.addStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderMapper.toResponse(order);
    }
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        return orderMapper.toResponse(order);
    }

    public Page<OrderResponse> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

}