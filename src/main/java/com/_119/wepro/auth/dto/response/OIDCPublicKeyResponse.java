package com._119.wepro.auth.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OIDCPublicKeyResponse {

  private List<OIDCPublicKey> keys;

  @Getter
  @NoArgsConstructor
  public static class OIDCPublicKey {

    private String kid;
    private String alg;
    private String use;
    private String n;
    private String e;
  }
}
