package service;

import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetDashboardResponse;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import com.boot.backend.Sweet.Shop.Management.System.service.DashboardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void shouldReturnCorrectDashboardStats() {
        // Arrange
        when(sweetRepository.countTotalSweets())
                .thenReturn(25L);

        when(sweetRepository.sumTotalStock())
                .thenReturn(420L);

        when(sweetRepository.countLowStock(5))
                .thenReturn(3L);

        // Act
        SweetDashboardResponse response =
                dashboardService.getSweetDashboardStats();

        // Assert
        assertEquals(25L, response.getTotalSweets());
        assertEquals(420, response.getTotalStock());
        assertEquals(3, response.getLowStockItems());

        // Verify interactions
        verify(sweetRepository, times(1)).countTotalSweets();
        verify(sweetRepository, times(1)).sumTotalStock();
        verify(sweetRepository, times(1)).countLowStock(5);
    }
}
