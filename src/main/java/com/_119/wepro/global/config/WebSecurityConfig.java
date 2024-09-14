package com._119.wepro.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com._119.wepro.global.filter.JwtTokenExceptionFilter;
import com._119.wepro.global.filter.JwtTokenFilter;
import com._119.wepro.global.security.CustomOidcAuthenticationSuccessHandler;
import com._119.wepro.global.security.CustomOidcUserService;
import com._119.wepro.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  //  private final CustomOidcUserService customOidcUserService;
//  private final ClientRegistrationRepository clientRegistrationRepository;
//  private final OAuth2AuthorizedClientRepository authorizedClientRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() { // 정적 리소스 제외
    return web -> web.ignoring()
        .requestMatchers("/css/**", "/images/**", "/js/**", "/lib/**")
        .requestMatchers("/swagger-ui-custom.html", "/api-docs/**", "/swagger-ui/**", "swagger-ui.html", "/v3/api-docs/**")
        .requestMatchers("/error", "/favicon.ico");
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
            .loginPage("http://localhost:3000/")
            .failureHandler(customAuthenticationFailureHandler)
            .successHandler(customOidcAuthenticationSuccessHandler())
        );

    http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
        UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtTokenExceptionFilter(), JwtTokenFilter.class);
    return http.build();
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf(AbstractHttpConfigurer::disable)
//        .formLogin(Customizer.withDefaults()) // ??
//
//        // 세션 사용 안함
//        .sessionManagement(c ->
//            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//        .authorizeHttpRequests(request -> request
//            .requestMatchers("/", "/login").permitAll() // 범인...은 너였어..
//            .requestMatchers(HttpMethod.OPTIONS).permitAll()
//            .anyRequest().authenticated()
//        )
//        // OAuth2 로그인 처리
//        .oauth2Login(oauth2Login -> oauth2Login
////            .clientRegistrationRepository(clientRegistrationRepository)
////            .authorizedClientRepository(authorizedClientRepository)
//                .loginPage("/login") // 로그인 필요 경로 요청 시 보낼 경로(로그인 페이지)
//                .userInfoEndpoint(userInfo -> userInfo
//                    .oidcUserService(customOidcUserService))
//                .successHandler(customOidcAuthenticationSuccessHandler()) // 커스텀 로그인 성공 핸들러 등록
//        )
////        .oauth2Client(Customizer.withDefaults()) // OAuth2 클라이언트 설정 추가
//        .logout(logout -> logout
//            .logoutUrl("/logout")
//            .logoutSuccessUrl("/")
//        );
//
////    http.getConfigurer(OAuth2AuthorizationServerAutoConfiguration.class).oidc(Customizer.withDefaults());
//    return http.build();
//  }
}
