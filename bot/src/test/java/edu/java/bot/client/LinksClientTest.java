package edu.java.bot.client;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import edu.java.bot.client.exception.BadResponseBodyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpsEnabled = true)
public class LinksClientTest {
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
                                                               .options(wireMockConfig().dynamicPort())
                                                               .build();
    private final String response = "{\"links\": [{\"id\": 1,\"uri\":\"https://example.com/link1\"}],\"size\": 1}";


    private LinksClient linksClient;

    @BeforeEach
    void init() {
        linksClient = new LinksClient(wireMockServer.baseUrl());
    }

    @Test
    void testGetLinksShouldReturnCorrectResponse() throws URISyntaxException, BadResponseBodyException {
        ListLinksResponse excepted = new ListLinksResponse(new LinkResponse[]{new LinkResponse(1L, new URI("https://example.com/link1"))}, 1L);
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .willReturn(WireMock.jsonResponse(response, 200)));
        ListLinksResponse actual = linksClient.getLinks(1L);
        assertThat(actual.linkResponses()).isEqualTo(excepted.linkResponses());
        assertThat(actual.size()).isEqualTo(excepted.size());

    }

    @Test
    void testGetLinksShouldReturnClientError() {
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .willReturn(WireMock.jsonResponse(response, 404)));
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> linksClient.getLinks(1L)
        );
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    void testGetLinksShouldReturnServerError() {
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .willReturn(WireMock.jsonResponse(response, 500)));
        HttpServerErrorException exception = assertThrows(
                HttpServerErrorException.class,
                () -> linksClient.getLinks(1L)
        );
        assertThat(exception.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }


    @Test
    void testDeleteLinkShouldReturnCorrectResponse() throws URISyntaxException, BadResponseBodyException {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        LinkResponse excepted = new LinkResponse(1L, new URI("https://example.com/link1"));
        wireMockServer.stubFor(WireMock.delete(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 200)));
        assertThat(linksClient.deleteLink(1L, new RemoveLinkRequest("https://example.com/link1"))).isEqualTo(excepted);
    }

    @Test
    void testDeleteLinkShouldReturnClientError() {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        wireMockServer.stubFor(WireMock.delete(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 404)));
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> linksClient.deleteLink(1L, new RemoveLinkRequest("https://example.com/link1"))
        );
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    void testDeleteLinkShouldReturnServerError() {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        wireMockServer.stubFor(WireMock.delete(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 500)));
        HttpServerErrorException exception = assertThrows(
                HttpServerErrorException.class,
                () -> linksClient.deleteLink(1L, new RemoveLinkRequest("https://example.com/link1"))
        );
        assertThat(exception.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }


    @Test
    void testAddLinkShouldReturnCorrectResponse() throws URISyntaxException, BadResponseBodyException {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        LinkResponse excepted = new LinkResponse(1L, new URI("https://example.com/link1"));
        wireMockServer.stubFor(WireMock.post(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 200)));
        assertThat(linksClient.addLink(1L, new AddLinkRequest("https://example.com/link1"))).isEqualTo(excepted);
    }

    @Test
    void testAddLinkShouldReturnClientError() {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        wireMockServer.stubFor(WireMock.post(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 404)));
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> linksClient.addLink(1L, new AddLinkRequest("https://example.com/link1"))
        );
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    void testAddLinkShouldReturnServerError() {
        String request = "{\"link\":\"https://example.com/link1\"}";
        String response = "{\"id\":1, \"uri\":\"https://example.com/link1\"}";
        wireMockServer.stubFor(WireMock.post(WireMock.anyUrl())
                                       .withHeader("Accept", WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                                       .withHeader("Tg-Chat-Id", WireMock.equalTo(String.valueOf(1L)))
                                       .withRequestBody(WireMock.equalToJson(request))
                                       .willReturn(WireMock.jsonResponse(response, 500)));
        HttpServerErrorException exception = assertThrows(
                HttpServerErrorException.class,
                () -> linksClient.addLink(1L, new AddLinkRequest("https://example.com/link1"))
        );
        assertThat(exception.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

}
