package com.udemy.photoapp.api.apigateway.filter;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    @Autowired
    private Environment environment;

    public AuthorizationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            var request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
            }
            var jwt = request.getHeaders()
                    .get(HttpHeaders.AUTHORIZATION)
                    .get(0)
                    .replace("Bearer", "");
            if(!isValidJWT(jwt)){
                return onError(exchange,"Invalid Token",HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };

    }

    private boolean isValidJWT(String jwt){
        var subject = Jwts.parser()
                .setSigningKey(environment.getProperty("token.secret"))
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
        if(Strings.isNullOrEmpty(subject)){
            return false;
        }
        return true;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String no_authorization_header, HttpStatus unauthorized) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    public static class Config {

    }
}
