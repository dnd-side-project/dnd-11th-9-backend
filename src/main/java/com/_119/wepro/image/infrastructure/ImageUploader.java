package com._119.wepro.image.infrastructure;

import com._119.wepro.image.domain.ImageFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class ImageUploader {

  private static final String CACHE_CONTROL_VALUE = "max-age=3153600";

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.s3.folder}")
  private String folder;

  public List<String> uploadImages(final List<ImageFile> imageFiles) {
    final List<CompletableFuture<String>> imageUploadsFuture = imageFiles.stream()
        .map(imageFile -> CompletableFuture.supplyAsync(() -> uploadImage(imageFile)))
        .toList();
    return getUploadedImageNamesFromFutures(imageUploadsFuture);
  }

  private List<String> getUploadedImageNamesFromFutures(List<CompletableFuture<String>> futures) {
    final List<String> fileNames = new ArrayList<>();
    futures.forEach(future -> {
      try {
        fileNames.add(future.join());
      } catch (final CompletionException ignored) {
      }
    });
    return fileNames;
  }

  private String uploadImage(final ImageFile imageFile) {
    final String path = folder + imageFile.getHashedName();
    final ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(imageFile.getContentType());
    metadata.setContentLength(imageFile.getSize());
    metadata.setCacheControl(CACHE_CONTROL_VALUE);

    try (final InputStream inputStream = imageFile.getInputStream()) {
      s3Client.putObject(bucket, path, inputStream, metadata);
    } catch (final AmazonServiceException e) {
      throw new ImageException(INVALID_IMAGE_PATH);
    } catch (final IOException e) {
      throw new ImageException(INVALID_IMAGE);
    }
    return imageFile.getHashedName();
  }
}
