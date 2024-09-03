package com._119.wepro.member.presentation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class memberController {
  @GetMapping("/")
  public String index(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
      @AuthenticationPrincipal OidcUser oauth2User) {
    model.addAttribute("userName", oauth2User.getName());
    model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
    model.addAttribute("userAttributes", oauth2User.getAttributes());

    return "index";
  }
}
