package com.aqua.flux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    // ✅ Allow frontend running on port 4000
    corsConfig.setAllowedOrigins(List.of("http://localhost:4000"));

    // ✅ Allow necessary HTTP methods
    corsConfig.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));

    // ✅ Allow necessary headers
    corsConfig.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Custom-Header"));

    // ✅ Allow credentials (cookies, auth tokens)
    corsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig); // ✅ Apply to all endpoints
    return new CorsFilter(source);
  }
}
