package edu.java.bot.client;

import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import edu.java.bot.client.exception.BadResponseBodyException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.bot.client.ClientStatusCodeHandler.ERROR_RESPONSE_FILTER;

@Slf4j
@Component
public class LinksClient {
    private final String tgChatIdHeader = "Tg-Chat-Id";
    private final WebClient webCLient;

    public LinksClient(
        @Value("${app.client.links-client.base-url}")
        @NotBlank @URL String url
    ) {
        this.webCLient = WebClient.builder().filter(ERROR_RESPONSE_FILTER).baseUrl(url).build();
    }

    public ListLinksResponse getLinks(Long tgChatId) throws BadResponseBodyException {
        try {
            return webCLient.get()
                .accept(MediaType.APPLICATION_JSON)
                .header(tgChatIdHeader, String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }

    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest request) throws BadResponseBodyException {
        try {
            return webCLient.method(HttpMethod.DELETE)
                .accept(MediaType.APPLICATION_JSON)
                .header(tgChatIdHeader, String.valueOf(tgChatId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }

    public LinkResponse addLink(Long tgChatId, AddLinkRequest request) throws BadResponseBodyException {
        try {
            return webCLient.post()
                .accept(MediaType.APPLICATION_JSON)
                .header(tgChatIdHeader, String.valueOf(tgChatId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }
}


