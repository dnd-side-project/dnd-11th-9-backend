package com._119.wepro.image.infrastructure;

import static com._119.wepro.global.exception.errorcode.ImageErrorCode.INVALID_IMAGE;
import static com._119.wepro.global.exception.errorcode.ImageErrorCode.INVALID_IMAGE_PATH;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.image.domain.ImageFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
public class ImageUploader {

  private static final String CACHE_CONTROL_VALUE = "max-age=3153600";

  private final S3Client s3Client;

  private final S3Utilities s3Utilities;

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

    try (final InputStream inputStream = imageFile.getInputStream()) {

      Map<String, String> metadata = new HashMap<>();
      metadata.put("content-type", imageFile.getContentType());
      metadata.put("content-length", "" + imageFile.getSize());
      metadata.put("cache-control", CACHE_CONTROL_VALUE);
      PutObjectRequest putOb = PutObjectRequest.builder()
          .bucket(bucket)
          .key(path)
          .metadata(metadata)
          .build();

      s3Client.putObject(putOb, RequestBody.fromInputStream(inputStream, imageFile.getSize()));
    } catch (final S3Exception e) {
      throw new RestApiException(INVALID_IMAGE_PATH);
    } catch (final IOException e) {
      throw new RestApiException(INVALID_IMAGE);
    }
    GetUrlRequest request = GetUrlRequest.builder().bucket(bucket).key(path).build();

    return s3Utilities.getUrl(request).toString();

  }
}