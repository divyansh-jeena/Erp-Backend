package com.example.erpsystem.auth.entity;

import com.example.erpsystem.auth.enums.Permission;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
public enum Role {

    ADMIN(EnumSet.allOf(Permission.class)),

    MANAGER(Set.of(
            // Orders
            Permission.CREATE_ORDER,
            Permission.VIEW_ORDER,
            Permission.UPDATE_ORDER,

            // Inventory
            Permission.VIEW_INVENTORY,
            Permission.UPDATE_INVENTORY,

            // Products
            Permission.VIEW_PRODUCT,
            Permission.UPDATE_PRODUCT,

            // Purchases
            Permission.CREATE_PURCHASE,
            Permission.VIEW_PURCHASE,
            Permission.UPDATE_PURCHASE,

            // Suppliers
            Permission.CREATE_SUPPLIER,
            Permission.VIEW_SUPPLIER,
            Permission.UPDATE_SUPPLIER,

            // Payments
            Permission.PROCESS_PAYMENT
    )),

    EMPLOYEE(Set.of(
            Permission.VIEW_ORDER,
            Permission.VIEW_INVENTORY,
            Permission.VIEW_PRODUCT,
            Permission.VIEW_PURCHASE,
            Permission.VIEW_SUPPLIER
    ));

    private final Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }
}