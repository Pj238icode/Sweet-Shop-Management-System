package service;

import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    // ✅ Use IMPLEMENTATION class
    @InjectMocks
    private com.boot.backend.Sweet.Shop.Management.System.service.InventoryServiceImpl inventoryService;

    @Test
    void shouldReduceStockWhenPurchaseIsValid() {
        // Arrange
        Sweet sweet = Sweet.builder()
                .id(1L)
                .quantity(20)
                .build();

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        // Act
        inventoryService.purchaseSweet(1L, 5);

        // Assert
        assertEquals(15, sweet.getQuantity());
    }
}
