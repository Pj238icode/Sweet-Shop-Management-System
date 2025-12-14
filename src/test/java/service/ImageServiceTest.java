package service;

import com.boot.backend.Sweet.Shop.Management.System.config.AwsProperties;
import com.boot.backend.Sweet.Shop.Management.System.service.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private AwsProperties awsProperties;

    @InjectMocks
    private ImageServiceImpl imageService;

    // =====================================================
    // SUCCESS CASE
    // =====================================================

    @Test
    void shouldUploadImageSuccessfully() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("sweet.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream())
                .thenReturn(new ByteArrayInputStream("dummy".getBytes()));

        when(awsProperties.getBucket()).thenReturn("test-bucket");

        doNothing().when(s3Client)
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));

        // Act
        String imageUrl = imageService.uploadImage(file);

        // Assert
        assertNotNull(imageUrl);
        assertTrue(imageUrl.startsWith(
                "https://test-bucket.s3.ap-south-1.amazonaws.com/Sweets/"
        ));

        verify(s3Client, times(1))
                .putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    // =====================================================
    // FAILURE CASE
    // =====================================================

    @Test
    void shouldThrowExceptionWhenUploadFails() throws Exception {
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("sweet.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream())
                .thenThrow(new RuntimeException("IO Error"));

        when(awsProperties.getBucket()).thenReturn("test-bucket");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imageService.uploadImage(file)
        );

        assertEquals("Failed to upload image to S3", exception.getMessage());
    }
}
