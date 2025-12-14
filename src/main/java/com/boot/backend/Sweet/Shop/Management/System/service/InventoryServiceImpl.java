package com.boot.backend.Sweet.Shop.Management.System.service;

import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import com.boot.backend.Sweet.Shop.Management.System.exception.InsufficientStockException;
import com.boot.backend.Sweet.Shop.Management.System.exception.SweetNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final SweetRepository sweetRepository;

    @Transactional
    public void purchaseSweet(Long sweetId, int qty) {

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new SweetNotFoundException(sweetId));

        if (sweet.getQuantity() < qty) {
            throw new InsufficientStockException(
                    "Available stock: " + sweet.getQuantity()
            );
        }

        sweet.setQuantity(sweet.getQuantity() - qty);
    }


    @Override
    @Transactional
    public void restockSweet(Long sweetId, int qty) {

        if (qty <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than zero");
        }

        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new SweetNotFoundException(sweetId));

        sweet.setQuantity(sweet.getQuantity() + qty);
    }

}
