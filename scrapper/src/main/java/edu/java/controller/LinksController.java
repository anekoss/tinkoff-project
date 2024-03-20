package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class LinksController {

    @GetMapping(path = "/links", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ListLinksResponse> getLinks(
        @RequestHeader(value = "Tg-Chat-Id") Long tgChatId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/links", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader(value = "Tg-Chat-Id") @NotNull Long tgChatId, @RequestBody @Valid RemoveLinkRequest request
    ) throws URISyntaxException {
        URI uri = new URI(request.link());
        return ResponseEntity.ok(new LinkResponse(tgChatId, uri));
    }

    @PostMapping(path = "/links", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader(value = "Tg-Chat-Id") @NotNull Long tgChatId,
        @RequestBody @Valid AddLinkRequest request
    ) throws URISyntaxException {
        URI uri = new URI(request.link());
        return ResponseEntity.ok(new LinkResponse(tgChatId, uri));

    }

}
