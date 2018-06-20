package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@SpringBootApplication
public class GatewayApplication {

  @Bean
  public MapReactiveUserDetailsService authentication() {
    return new MapReactiveUserDetailsService(
        User.withUsername("lillard")
            .password("lillard666")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .roles("USER")
            .build()
    );
  }

  @Bean
  public SecurityWebFilterChain authorization(ServerHttpSecurity security) {
    return security
        .authorizeExchange().pathMatchers("/rl/**").authenticated()
        .anyExchange().permitAll()
        .and()
        .httpBasic()
        .and()
        .build();
  }

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        // basic proxy
        .route(
            "start_route",
            p -> p
                .path("/start")
                .uri("http://start.spring.io:80/")
        )
            // load balance proxy & rewrite
        .route(
            "lb_rewrite_route",
            p -> p
                .path("/lb/**")
                .filters(f -> f.rewritePath("/lb/(?<path>.*)", "/${path}"))
                .uri("lb://customer-service/")
        )
            // stripPrefix & setStatus
        .route(
            "set_status_route",
            p -> p
                .path("/cf1/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://customer-service/")
                .filter(
                    (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
                      exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
                      exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PDF);
                    }))
                )
        )
            // circuit breaker
        .route(
            "circuit_breaker_route",
            p -> p
                .path("/cb/**")
                .filters(
                    f -> f
                        .stripPrefix(1)
                        .hystrix(config -> config.setName("cb"))
                )
                .uri("lb://customer-service/")
        )
            // rate limiter
        .route(
            "limit_route",
            p -> p
                .path("/rl/**")
                .filters(
                    f -> f
                        .stripPrefix(1)
                        .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()))
                )
                .uri("lb://customer-service/")
        )
        .build();
  }

  @Bean
  RedisRateLimiter redisRateLimiter() {
    return new RedisRateLimiter(5, 10);
  }

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
