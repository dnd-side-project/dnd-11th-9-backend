package com._119.wepro.image.service;

import static com._119.wepro.global.exception.errorcode.ImageErrorCode.EMPTY_IMAGE_LIST;
import static com._119.wepro.global.exception.errorcode.ImageErrorCode.EXCEED_IMAGE_LIST_SIZE;
import static com._119.wepro.global.exception.errorcode.ImageErrorCode.INVALID_IMAGE_PATH;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.image.domain.ImageFile;
import com._119.wepro.image.domain.S3ImageEvent;
import com._119.wepro.image.dto.response.ImagesResponse;
import com._119.wepro.image.infrastructure.ImageUploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

  private static final int MAX_IMAGE_LIST_SIZE = 5;
  private static final int EMPTY_LIST_SIZE = 0;

  private final ImageUploader imageUploader;
  private final ApplicationEventPublisher publisher;

  public ImagesResponse save(final List<MultipartFile> images) {
    validateSizeOfImages(images);
    final List<ImageFile> imageFiles = images.stream()
        .map(ImageFile::new)
        .toList();
    final List<String> imageUrls = uploadImages(imageFiles);
    return new ImagesResponse(imageUrls);
  }

  private List<String> uploadImages(final List<ImageFile> imageFiles) {
    try {
      final List<String> uploadedImageNames = imageUploader.uploadImages(imageFiles);
      if(uploadedImageNames.size() != imageFiles.size()) {
        throw new RestApiException(INVALID_IMAGE_PATH);
      }
      return uploadedImageNames;
    } catch (final RestApiException e) {
      imageFiles.forEach(imageFile -> publisher.publishEvent(new S3ImageEvent(imageFile.getHashedName())));
      throw e;
    }
  }

  private void validateSizeOfImages(final List<MultipartFile> images) {
    if (images.size() > MAX_IMAGE_LIST_SIZE) {
      throw new RestApiException(EXCEED_IMAGE_LIST_SIZE);
    }
    if (images.isEmpty()) {
      throw new RestApiException(EMPTY_IMAGE_LIST);
    }
  }
}