package com._119.wepro.listener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import com._119.wepro.image.domain.S3ImageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Component
@RequiredArgsConstructor
public class S3ImageEventListener {

  private static final String DEFAULT_IMAGE_NAME = "default-image.png";

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.s3.folder}")
  private String folder;

  @Async
  @Transactional(propagation = REQUIRES_NEW)
  @TransactionalEventListener(fallbackExecution = true)
  public void deleteImageFileInS3(final S3ImageEvent event) {
    final String imageName = event.getImageName();
    if (imageName.equals(DEFAULT_IMAGE_NAME)) {
      return;
    }

    s3Client.deleteObject(
        DeleteObjectRequest.builder().bucket(bucket).key(folder + imageName).build());
  }
}
