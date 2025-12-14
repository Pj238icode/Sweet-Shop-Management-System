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

    @Override
    @Transactional
    public void purchaseSweet(Long sweetId, int qty) {

        // 1️⃣ Validate quantity
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // 2️⃣ Fetch sweet or fail
        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new SweetNotFoundException(sweetId));

        // 3️⃣ Defensive check (extra safe)
        if (sweet.getQuantity() == null) {
            throw new IllegalStateException("Sweet quantity is not initialized");
        }

        // 4️⃣ Check available stock
        if (sweet.getQuantity() < qty) {
            throw new InsufficientStockException(
                    "Available stock: " + sweet.getQuantity()
            );
        }

        // 5️⃣ Update stock
        sweet.setQuantity(sweet.getQuantity() - qty);

        // 6️⃣ Explicit save (helps tests & clarity)
        sweetRepository.save(sweet);
    }

    @Override
    @Transactional
    public void restockSweet(Long sweetId, int qty) {

        // 1️⃣ Validate quantity
        if (qty <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than zero");
        }

        // 2️⃣ Fetch sweet or fail
        Sweet sweet = sweetRepository.findById(sweetId)
                .orElseThrow(() -> new SweetNotFoundException(sweetId));

        // 3️⃣ Defensive check
        if (sweet.getQuantity() == null) {
            throw new IllegalStateException("Sweet quantity is not initialized");
        }

        // 4️⃣ Update stock
        sweet.setQuantity(sweet.getQuantity() + qty);

        // 5️⃣ Explicit save
        sweetRepository.save(sweet);
    }
}
