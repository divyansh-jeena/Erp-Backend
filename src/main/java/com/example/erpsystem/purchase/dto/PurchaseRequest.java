package com.example.erpsystem.purchase.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;


public record PurchaseRequest(@NotNull Long supplierId, @NotEmpty List<PurchaseItemRequest> items) {

}
