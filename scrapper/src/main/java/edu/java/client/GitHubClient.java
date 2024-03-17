package edu.java.client;

import edu.java.client.dto.GitHubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class GitHubClient {

    private final String defaultUrl = "https://api.github.com";
    private final WebClient webCLient;

    public GitHubClient(String url) {
        this.webCLient = WebClient.builder().baseUrl(url).build();
    }

    public GitHubClient() {
        this.webCLient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public GitHubResponse fetchRepository(String owner, String repo) {
        GitHubResponse response =
            webCLient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .onErrorMap(error -> {
                    log.error(error.getMessage());
                    throw new IllegalArgumentException("No response body was returned from the service");
                })
                .block();
        return response;
    }

}
