package service;

import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import com.boot.backend.Sweet.Shop.Management.System.exception.InsufficientStockException;
import com.boot.backend.Sweet.Shop.Management.System.exception.SweetNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import com.boot.backend.Sweet.Shop.Management.System.service.InventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    // =====================================================
    // PASSING CASE
    // =====================================================

    @Test
    void shouldReduceStockWhenPurchaseIsValid() {
        Sweet sweet = Sweet.builder()
                .id(1L)
                .quantity(20)
                .build();

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        inventoryService.purchaseSweet(1L, 5);

        assertEquals(15, sweet.getQuantity());
    }

    // =====================================================
    // FAILING CASES
    // =====================================================

    @Test
    void shouldThrowExceptionWhenSweetNotFound() {
        when(sweetRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(SweetNotFoundException.class,
                () -> inventoryService.purchaseSweet(1L, 5));
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        Sweet sweet = Sweet.builder()
                .id(1L)
                .quantity(3)
                .build();

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        assertThrows(InsufficientStockException.class,
                () -> inventoryService.purchaseSweet(1L, 5));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        Sweet sweet = Sweet.builder()
                .id(1L)
                .quantity(10)
                .build();

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.purchaseSweet(1L, 0));

        assertThrows(IllegalArgumentException.class,
                () -> inventoryService.purchaseSweet(1L, -2));
    }
}
