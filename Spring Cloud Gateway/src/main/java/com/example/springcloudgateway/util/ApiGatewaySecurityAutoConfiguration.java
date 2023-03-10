package com.example.springcloudgateway.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@EnableWebFluxSecurity
@Configuration
@Slf4j
public class ApiGatewaySecurityAutoConfiguration {

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable().cors().and().authorizeExchange(exchanges ->
                exchanges.anyExchange().permitAll()
        ).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            GlobalCorsProperties globalCorsProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        globalCorsProperties.getCorsConfigurations()
                .forEach(source::registerCorsConfiguration);
        return source;
    }

//    @Bean
//    public CorsConfiguration corsConfiguration(RoutePredicateHandlerMapping routePredicateHandlerMapping) {
//        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
//        Arrays.asList(HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.POST).forEach(corsConfiguration::addAllowedMethod);
//        corsConfiguration.addAllowedOrigin("*");
//        routePredicateHandlerMapping.setCorsConfigurations(new HashMap<>() {{
//            put("/**", corsConfiguration);
//        }});
//        return corsConfiguration;
//    }
}
