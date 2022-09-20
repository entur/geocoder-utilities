package org.entur.geocoder.utilities.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlerRouteBuilder extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerRouteBuilder.class);

    private final int maxRedelivery;
    private final int redeliveryDelay;
    private final int backOffMultiplier;

    public ErrorHandlerRouteBuilder(int maxRedelivery, int redeliveryDelay, int backOffMultiplier) {
        this.maxRedelivery = maxRedelivery;
        this.redeliveryDelay = redeliveryDelay;
        this.backOffMultiplier = backOffMultiplier;
    }

    @Override
    public void configure() {

        errorHandler(defaultErrorHandler()
                .redeliveryDelay(redeliveryDelay)
                .maximumRedeliveries(maxRedelivery)
                .onRedelivery(ErrorHandlerRouteBuilder::logRedelivery)
                .useExponentialBackOff()
                .backOffMultiplier(backOffMultiplier)
                .logExhausted(true)
                .logRetryStackTrace(true));
    }

    private static void logRedelivery(Exchange exchange) {
        var redeliveryCounter = exchange.getIn().getHeader("CamelRedeliveryCounter", Integer.class);
        var redeliveryMaxCounter = exchange.getIn().getHeader("CamelRedeliveryMaxCounter", Integer.class);
        var camelCaughtThrowable = exchange.getProperty("CamelExceptionCaught", Throwable.class);

        logger.warn("Exchange failed, redelivering the message locally, attempt {}/{}...",
                redeliveryCounter, redeliveryMaxCounter, camelCaughtThrowable);
    }
}
