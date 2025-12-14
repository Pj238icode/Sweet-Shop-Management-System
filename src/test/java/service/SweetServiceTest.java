package service;

import com.boot.backend.Sweet.Shop.Management.System.dto.request.SweetRequest;
import com.boot.backend.Sweet.Shop.Management.System.dto.response.SweetResponse;
import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import com.boot.backend.Sweet.Shop.Management.System.exception.SweetNotFoundException;
import com.boot.backend.Sweet.Shop.Management.System.repository.SweetRepository;
import com.boot.backend.Sweet.Shop.Management.System.service.ImageService;
import com.boot.backend.Sweet.Shop.Management.System.service.SweetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SweetServiceImplTest {

    @Mock
    private SweetRepository sweetRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private SweetServiceImpl sweetService;

    // ---------- ADD SWEET ----------

    @Test
    void shouldAddSweetSuccessfully() {
        SweetRequest request = new SweetRequest(
                "Ladoo",
                "Indian",
                20.0,
                100
        );

        MultipartFile image = mock(MultipartFile.class);

        when(imageService.uploadImage(image))
                .thenReturn("image-url");

        when(sweetRepository.save(any(Sweet.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        SweetResponse response =
                sweetService.addSweet(request, image);

        assertNotNull(response);
        assertEquals("Ladoo", response.getName());
        assertEquals(100, response.getQuantity());
        assertEquals("image-url", response.getImageUrl());
    }

    // ---------- GET ALL SWEETS ----------

    @Test
    void shouldReturnPaginatedSweets() {
        Sweet sweet = Sweet.builder()
                .id(1L)
                .name("Barfi")
                .price(30.0)
                .quantity(50)
                .build();

        Page<Sweet> page =
                new PageImpl<>(List.of(sweet));

        when(sweetRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<SweetResponse> result =
                sweetService.getAllSweets(0, 10, "id", "asc");

        assertEquals(1, result.getTotalElements());
        assertEquals("Barfi", result.getContent().get(0).getName());
    }

    // ---------- UPDATE SWEET ----------

    @Test
    void shouldUpdateSweetSuccessfully() {
        Sweet sweet = Sweet.builder()
                .id(1L)
                .name("Old")
                .quantity(10)
                .build();

        SweetRequest request = new SweetRequest(
                "New",
                "Indian",
                50.0,
                30
        );

        MultipartFile image = mock(MultipartFile.class);

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        when(imageService.uploadImage(image))
                .thenReturn("new-image");

        SweetResponse response =
                sweetService.updateSweet(1L, request, image);

        assertEquals("New", response.getName());
        assertEquals(30, response.getQuantity());
        assertEquals("new-image", response.getImageUrl());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingSweet() {
        when(sweetRepository.findById(1L))
                .thenReturn(Optional.empty());

        SweetRequest request =
                new SweetRequest("Name", "Cat", 10.0, 5);

        assertThrows(SweetNotFoundException.class,
                () -> sweetService.updateSweet(1L, request, null));
    }

    // ---------- DELETE SWEET ----------

    @Test
    void shouldDeleteSweetSuccessfully() {
        Sweet sweet = Sweet.builder().id(1L).build();

        when(sweetRepository.findById(1L))
                .thenReturn(Optional.of(sweet));

        sweetService.deleteSweet(1L);

        verify(sweetRepository).delete(sweet);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSweet() {
        when(sweetRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(SweetNotFoundException.class,
                () -> sweetService.deleteSweet(1L));
    }

    // ---------- SEARCH SWEETS ----------

    @Test
    void shouldSearchSweetsByName() {
        Sweet sweet = Sweet.builder()
                .name("Ladoo")
                .build();

        when(sweetRepository.findByNameContainingIgnoreCase("Ladoo"))
                .thenReturn(List.of(sweet));

        List<SweetResponse> result =
                sweetService.searchSweets("Ladoo", null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    void shouldSearchSweetsByCategory() {
        Sweet sweet = Sweet.builder()
                .category("Indian")
                .build();

        when(sweetRepository.findByCategoryIgnoreCase("Indian"))
                .thenReturn(List.of(sweet));

        List<SweetResponse> result =
                sweetService.searchSweets(null, "Indian", null, null);

        assertEquals(1, result.size());
    }

    @Test
    void shouldSearchSweetsByPriceRange() {
        Sweet sweet = Sweet.builder()
                .price(25.0)
                .build();

        when(sweetRepository.findByPriceBetween(10.0, 30.0))
                .thenReturn(List.of(sweet));

        List<SweetResponse> result =
                sweetService.searchSweets(null, null, 10.0, 30.0);

        assertEquals(1, result.size());
    }
}
