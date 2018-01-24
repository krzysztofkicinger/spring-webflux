package com.kicinger.spring.webflux.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class QuoteRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(QuoteHandlers quoteHandlers) {
        return RouterFunctions
                .route(GET("/hello"), quoteHandlers::hello)
                .andRoute(POST("/echo").and(contentType(MediaType.TEXT_PLAIN)), quoteHandlers::echo)
                .andRoute(GET("/quotes"), quoteHandlers::streamQuotes);
    }

}
