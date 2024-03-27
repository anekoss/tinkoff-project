package edu.java.bot.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class ClientStatusCodeHandler {
    public static final ExchangeFilterFunction ERROR_RESPONSE_FILTER = ExchangeFilterFunction
            .ofResponseProcessor(ClientStatusCodeHandler::exchangeFilterResponseProcessor);

    private ClientStatusCodeHandler() {

    }

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        if (response.statusCode().is5xxServerError()) {
            log.error(response.statusCode() + " SERVER_ERROR");
            return Mono.error(new HttpServerErrorException(response.statusCode()));
        } else if (response.statusCode().is4xxClientError()) {
            log.error(response.statusCode() + " CLIENT_ERROR");
            return Mono.error(new HttpClientErrorException(response.statusCode()));
        }
        return Mono.just(response);
    }
}
