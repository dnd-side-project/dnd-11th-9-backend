package com._119.wepro.global.util;

import static com._119.wepro.global.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com._119.wepro.global.exception.RestApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public Long getCurrentMemberId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    try {
      // Todo : getName = provider Id이므로 수정할 것 
      return Long.parseLong(authentication.getName());
    } catch (Exception e) {
      throw new RestApiException(USER_NOT_FOUND);
    }
  }
}
