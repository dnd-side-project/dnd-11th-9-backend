package com._119.wepro.image.dto.response;

import com._119.wepro.image.domain.Image;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImagesResponse {

  private List<String> imageUrls;

  public static ImagesResponse of(final List<Image> images) {
    return new ImagesResponse(
        images.stream()
            .map(Image::getUrl)
            .toList());
  }
}