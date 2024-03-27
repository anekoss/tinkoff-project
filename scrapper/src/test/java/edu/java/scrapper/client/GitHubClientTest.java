package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.GitHubClient;
import edu.java.client.dto.GitHubResponse;
import edu.java.client.exception.BadResponseBodyException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubClientTest extends IntegrationTest {
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
                                                               .options(wireMockConfig().dynamicPort())
                                                               .build();
    private final Path okResponsePath = Path.of("src/test/java/edu/java/scrapper/client/github/github_ok.json");
    private final Path badResponsePath = Path.of("src/test/java/edu/java/scrapper/client/github/github_bad.json");
    @Autowired
    private GitHubClient gitHubClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.github.base-url", wireMockServer::baseUrl);
    }

    @Test
    void testGetRepositoryShouldReturnCorrectResponse() throws IOException, BadResponseBodyException {
        String response = String.join("", Files.readAllLines(okResponsePath));
        wireMockServer.stubFor(WireMock.get("/repos/anekoss/tinkoff-project")
                                       .willReturn(aResponse().withStatus(200)
                                                              .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                                              .withBody(response))
        );
        GitHubResponse excepted = new GitHubResponse(
                755879115L,
                "tinkoff-project",
                "anekoss/tinkoff-project",
                OffsetDateTime.parse("2024-02-11T11:13:17Z"),
                OffsetDateTime.parse("2024-02-21T12:54:35Z"),
                OffsetDateTime.parse("2024-02-11T11:13:57Z")
        );
        assertThat(gitHubClient.fetchRepository("anekoss", "tinkoff-project")).isEqualTo(excepted);
    }

    @Test
    void testGetRepositoryShouldReturnClientError() throws IOException {
        String response = String.join("", Files.readAllLines(badResponsePath));
        wireMockServer.stubFor(WireMock.get("/repos/anekoss/tinkoff-project")
                                       .willReturn(aResponse().withStatus(404)
                                                              .withHeader(
                                                                      "Content-Type",
                                                                      MediaType.APPLICATION_JSON_VALUE
                                                              )
                                                              .withBody(response))
        );
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> gitHubClient.fetchRepository("anekoss", "tinkoff-project")
        );
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    void testGetRepositoryShouldReturnServerError() throws IOException {
        String response = String.join("", Files.readAllLines(badResponsePath));
        wireMockServer.stubFor(WireMock.get("/repos/anekoss/tinkoff-project")
                                       .willReturn(aResponse().withStatus(500)
                                                              .withHeader(
                                                                      "Content-Type",
                                                                      MediaType.APPLICATION_JSON_VALUE
                                                              )
                                                              .withBody(response))
        );
        HttpServerErrorException exception = assertThrows(
                HttpServerErrorException.class,
                () -> gitHubClient.fetchRepository("anekoss", "tinkoff-project")
        );
        assertThat(exception.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void testGetRepositoryShouldReturnBadResponseBody() {
        wireMockServer.stubFor(WireMock.get("/repos/anekoss/tinkoff-project")
                                       .willReturn(aResponse().withStatus(200)
                                                              .withHeader(
                                                                      "Content-Type",
                                                                      MediaType.APPLICATION_JSON_VALUE
                                                              )
                                                              .withBody("{id:mewmew}")));
        BadResponseBodyException exception = assertThrows(BadResponseBodyException.class, () -> gitHubClient.fetchRepository("anekoss", "tinkoff-project"));
        assertThat(exception.getMessage()).isEqualTo("Bad response body was returned from the service");
    }


}
