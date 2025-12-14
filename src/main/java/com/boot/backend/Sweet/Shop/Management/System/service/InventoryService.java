package com.boot.backend.Sweet.Shop.Management.System.service;

public interface InventoryService {

    void purchaseSweet(Long sweetId, int qty);

    void restockSweet(Long sweetId, int qty);
}
