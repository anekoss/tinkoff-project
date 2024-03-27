package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
public class BotController {
    @PostMapping(path = "/updates", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> linkUpdate(@RequestBody @Valid LinkUpdateRequest request) throws URISyntaxException {
        URI uri = new URI(request.url());
        return ResponseEntity.ok().build();
    }

}
