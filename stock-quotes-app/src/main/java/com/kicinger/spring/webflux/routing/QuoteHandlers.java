package com.kicinger.spring.webflux.routing;

import com.kicinger.spring.webflux.domain.Quote;
import com.kicinger.spring.webflux.generators.QuoteGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
public class QuoteHandlers {

    private Flux<Quote> quotesStream;

    @Autowired
    public QuoteHandlers(QuoteGenerator quoteGenerator) {
        this.quotesStream = quoteGenerator.fetchQuoteStream(Duration.ofMillis(5000)).share();
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromObject("Hello Spring!"));
    }

    public Mono<ServerResponse> echo(ServerRequest request) {
        return request.bodyToMono(String.class)
                    .flatMap(body -> ServerResponse.ok()
                            .contentType(request.headers().contentType().orElse(MediaType.TEXT_PLAIN))
                            .body(fromObject(body)));
    }

    public Mono<ServerResponse> streamQuotes(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(fromPublisher(quotesStream, Quote.class));
    }

}
