package com.example.springcloudgateway;

import com.example.springcloudgateway.security.RouterValidator;
import com.example.springcloudgateway.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {


    @Autowired
    private RouterValidator routerValidator;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("Gateway filter");
            log.info(exchange.getRequest().getPath().toString());
            ServerHttpRequest request = exchange.getRequest();
            if(routerValidator.isSecured.test(request)) {
                if (this.isAuthMissing(request)) {
                    return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
                }
                final String token = this.getAuthHeader(request);
                final boolean isTokenValid = jwtTokenUtil.validateToken(token.split(" ")[1]);
                if (!isTokenValid) {
                    return this.onError(exchange, "Authorization token is not valid", HttpStatus.UNAUTHORIZED);
                }
            }
            log.info("Filter not applied.");
                return chain.filter(exchange);
        });
    }
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    public static class Config {
        // ...
    }
    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}