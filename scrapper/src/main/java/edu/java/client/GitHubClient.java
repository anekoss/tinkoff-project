package edu.java.client;

import edu.java.client.dto.GitHubResponse;
import edu.java.client.exception.BadResponseBodyException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.client.ClientStatusCodeHandler.ERROR_RESPONSE_FILTER;

@Slf4j
@Component
public class GitHubClient {

    private final WebClient webCLient;

    public GitHubClient(
            @Value("${app.client.github.base-url}")
            @NotBlank @URL String url) {
        this.webCLient = WebClient.builder().filter(ERROR_RESPONSE_FILTER).baseUrl(url).build();
    }


    public GitHubResponse fetchRepository(String owner, String repo) throws BadResponseBodyException {
        try {
            return webCLient.get()
                            .uri("/repos/{owner}/{repo}", owner, repo)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(GitHubResponse.class)
                            .block();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadResponseBodyException();
        }
    }

}
