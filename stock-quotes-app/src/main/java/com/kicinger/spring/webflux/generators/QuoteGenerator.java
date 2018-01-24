package com.kicinger.spring.webflux.generators;

import com.kicinger.spring.webflux.domain.Quote;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class QuoteGenerator {

    private static final MathContext MATH_CONTEXT = new MathContext(2);
    private Random random = new Random();
    private List<Quote> prices = new ArrayList<>();

    public QuoteGenerator() {
        this.prices.add(new Quote("CTXS", 82.26));
        this.prices.add(new Quote("DELL", 63.74));
        this.prices.add(new Quote("GOOG", 847.24));
        this.prices.add(new Quote("MSFT", 65.11));
        this.prices.add(new Quote("ORCL", 45.71));
        this.prices.add(new Quote("RHT", 84.29));
        this.prices.add(new Quote("VMW", 92.21));
    }

    public Flux<Quote> fetchQuoteStream(Duration period) {
        return Flux.generate(
                    () -> 0,
                    (index, sink) -> {
                        Quote updatedQuote = updateQuote(this.prices.get(index));
                        sink.next(updatedQuote);
                        return ++index % this.prices.size();
                    }
                )
                .zipWith(Flux.interval(period))
                .map(t -> (Quote) t.getT1())
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                })
                .log("Stock Quote Workshop Application");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange = quote.getPrice().multiply(new BigDecimal(0.05 * this.random.nextDouble()), MATH_CONTEXT);
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }

}
