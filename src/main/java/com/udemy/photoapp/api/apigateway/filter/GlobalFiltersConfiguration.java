package com.udemy.photoapp.api.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfiguration {

    private Logger logger = LoggerFactory.getLogger(GlobalFiltersConfiguration.class);

    @Order(1)
    @Bean
    public GlobalFilter getFilter() {
        return (exchange, chain) -> {
            logger.info("GlobalFilter : Pre-Filter executed");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logger.info("GlobalFilter : Post-Filter Executed");
                    }));
        };
    }

}
