package com._119.wepro.global.config;

import com._119.wepro.auth.jwt.JwtTokenExceptionFilter;
import com._119.wepro.auth.jwt.JwtTokenFilter;
import com._119.wepro.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
    return web -> web.ignoring()
        .requestMatchers("/css/**", "/images/**", "/js/**", "/lib/**")
        .requestMatchers("/", "/swagger-ui-custom.html", "/api-docs/**", "/swagger-ui/**",
            "swagger-ui.html", "/v3/api-docs/**")
        .requestMatchers("/error", "/favicon.ico")
        .requestMatchers("/auth/**", "/login/oauth2/code/**", "/login", "/refresh");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/auth/**", "/login/oauth2/code/**", "/login").permitAll()
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            .anyRequest().authenticated()
        )
        .logout(logout -> logout.disable())
        .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtTokenExceptionFilter(), JwtTokenFilter.class);
    return http.build();
  }
}
