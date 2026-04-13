package com.example.erpsystem.sales.dto;

import java.util.List;

public record OrderRequest(long userId,List<OrderItemRequest >items) {
}
