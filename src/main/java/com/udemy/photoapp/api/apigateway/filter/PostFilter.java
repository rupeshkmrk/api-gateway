package com.udemy.photoapp.api.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PostFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Logger logger = LoggerFactory.getLogger(PostFilter.class);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(()->{
                    logger.info("Inside Post Filter");
                }));
    }
}
