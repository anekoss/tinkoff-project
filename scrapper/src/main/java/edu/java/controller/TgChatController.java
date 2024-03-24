package edu.java.controller;

import edu.java.controller.exception.AlreadyRegisterException;
import edu.java.controller.exception.ChatNotFoundException;
import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TgChatController {
    private final TgChatService chatService;

    @PostMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(@PathVariable("id") Long id) throws AlreadyRegisterException {
        chatService.register(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable("id") Long id) throws ChatNotFoundException {
        chatService.unregister(id);
        return ResponseEntity.ok().build();
    }

}
