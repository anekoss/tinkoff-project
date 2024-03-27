package edu.java.bot.client;

import edu.java.bot.client.exception.BadResponseBodyException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.bot.client.ClientStatusCodeHandler.ERROR_RESPONSE_FILTER;


@Slf4j
@Component
public class TgChatClient {

    private final String pathId = "/{id}";
    private final WebClient webCLient;

    public TgChatClient(
            @Value("${app.client.tg-сhat-client.base-url}")
            @NotBlank @URL String url
    ) {
        this.webCLient = WebClient.builder().filter(ERROR_RESPONSE_FILTER).baseUrl(url).build();
    }

    public Void registerChat(Long id) throws BadResponseBodyException {
        try {

            return webCLient.post()
                            .uri(pathId, id)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }

    public Void deleteChat(Long id) throws BadResponseBodyException {
        try {
            return webCLient.delete()
                            .uri(pathId, id)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }

}
