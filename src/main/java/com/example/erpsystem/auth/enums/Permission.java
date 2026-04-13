package com.example.erpsystem.auth.enums;

public enum Permission {

    // Orders
    CREATE_ORDER,
    VIEW_ORDER,
    UPDATE_ORDER,
    DELETE_ORDER,

    // Inventory
    VIEW_INVENTORY,
    UPDATE_INVENTORY,
    DELETE_INVENTORY,

    // Products
    CREATE_PRODUCT,
    VIEW_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,

    // Purchases
    CREATE_PURCHASE,
    VIEW_PURCHASE,
    UPDATE_PURCHASE,
    APPROVE_PURCHASE,

    // Suppliers
    CREATE_SUPPLIER,
    VIEW_SUPPLIER,
    UPDATE_SUPPLIER,
    DELETE_SUPPLIER,

    // Payments
    PROCESS_PAYMENT,

    // Users
    MANAGE_USERS
}