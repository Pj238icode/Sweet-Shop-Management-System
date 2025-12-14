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
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private AwsProperties awsProperties;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImageServiceImpl imageService;

    // =====================================================
    // SUCCESS CASE
    // =====================================================
    @Test
    void shouldUploadImageSuccessfully() throws Exception {

        when(awsProperties.getBucket()).thenReturn("test-bucket");
        when(file.getOriginalFilename()).thenReturn("test.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn(4L);
        when(file.getInputStream())
                .thenReturn(new ByteArrayInputStream("data".getBytes()));

        // ✅ Correct way (method is NOT void)
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String url = imageService.uploadImage(file);

        assertNotNull(url);
        assertTrue(url.contains("test-bucket"));
    }

    // =====================================================
    // FAILURE CASE
    // =====================================================
    @Test
    void shouldThrowExceptionWhenUploadFails() throws Exception {

        when(file.getOriginalFilename()).thenReturn("test.png");

        // ❗ Exception happens HERE → do not stub anything else
        when(file.getInputStream()).thenThrow(new IOException("IO Error"));

        assertThrows(RuntimeException.class,
                () -> imageService.uploadImage(file));
    }
}
