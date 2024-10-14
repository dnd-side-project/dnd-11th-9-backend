package com._119.wepro.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com._119.wepro.global.filter.JwtTokenExceptionFilter;
import com._119.wepro.global.filter.JwtTokenFilter;
import com._119.wepro.global.handler.CustomLogoutHandler;
import com._119.wepro.global.handler.CustomLogoutSuccessHandler;
import com._119.wepro.global.security.CustomOidcAuthenticationSuccessHandler;
import com._119.wepro.global.security.CustomOidcUserService;
import com._119.wepro.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomOidcUserService customOidcUserService;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() { // 정적 리소스 제외
    return web -> web.ignoring()
        .requestMatchers("/css/**", "/images/**", "/js/**", "/lib/**")
        .requestMatchers("/swagger-ui-custom.html", "/api-docs/**", "/swagger-ui/**",
            "swagger-ui.html", "/v3/api-docs/**")
        .requestMatchers("/error", "/favicon.ico")
        .requestMatchers("/members/reissue");
  }

  @Bean
  public CustomOidcAuthenticationSuccessHandler customOidcAuthenticationSuccessHandler() {
    return new CustomOidcAuthenticationSuccessHandler(jwtTokenProvider);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults())
        .sessionManagement(c ->
            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(withDefaults())
        .oauth2Login(oauth2Login -> oauth2Login
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                .failureHandler(customAuthenticationFailureHandler)
                .successHandler(customOidcAuthenticationSuccessHandler())
        )
        .logout(logoutConfigurer -> logoutConfigurer
            .logoutUrl("/logout")
            .addLogoutHandler(new CustomLogoutHandler(jwtTokenProvider))
            .logoutSuccessHandler(new CustomLogoutSuccessHandler())
        );

    http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
            LogoutFilter.class)
        .addFilterBefore(new JwtTokenExceptionFilter(), JwtTokenFilter.class);
    return http.build();
  }
}
