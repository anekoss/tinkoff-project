package edu.java.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.StackOverflowResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StackOverflowClientTest {
    @Autowired
    private StackOverflowClient stackOverflowClient;
    private final Path okResponsePath = Path.of("src/test/java/edu/java/scrapper/stackOverflow/stackOverflow_ok.json");
    private final Path badResponsePath =
        Path.of("src/test/java/edu/java/scrapper/stackOverflow/stackOverflow_bad.json");

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.client.stackOverflow.base-url", wireMockServer::baseUrl);
    }

    @Test
    void testGetRepositoryShouldReturnCorrectResponse() throws IOException {
        String response =
            String.join("", Files.readAllLines(okResponsePath));
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{id}"))
            .withPathParam("id", WireMock.equalTo("78056352"))
            .withQueryParam("sort", WireMock.equalTo("activity"))
            .withQueryParam("site", WireMock.equalTo("stackoverflow"))
            .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                .withBody(response)));
        StackOverflowResponse stackOverflowResponse =
            new StackOverflowResponse(List.of(new StackOverflowResponse.StackOverflowItem(78056352L,
                "React Leaflet map not Re-rendering",
                "https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering",
                OffsetDateTime.parse("2024-02-25T14:38:10Z"), OffsetDateTime.parse("2024-02-25T14:38:10Z")
            )));
        assertThat(stackOverflowClient.fetchQuestion(78056352)).isEqualTo(stackOverflowResponse);
    }

    @Test
    void testGetQuestionsShouldReturnError() throws IOException {
        String response =
            String.join("", Files.readAllLines(badResponsePath));
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{id}"))
            .withPathParam("id", WireMock.equalTo("78056352"))
            .withQueryParam("sort", WireMock.equalTo("activity"))
            .withQueryParam("site", WireMock.equalTo("stackoverflow"))
            .willReturn(WireMock.aResponse().withStatus(404).withHeader("Content-Type", "application/json")
                .withBody(response)));
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> stackOverflowClient.fetchQuestion(78056353)
        );
        assertThat(exception.getMessage()).isEqualTo("No response body was returned from the service");
    }

}
