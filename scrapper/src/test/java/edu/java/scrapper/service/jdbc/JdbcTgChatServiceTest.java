package edu.java.scrapper.service.jdbc;

import edu.java.controller.exception.AlreadyRegisterException;
import edu.java.controller.exception.ChatNotFoundException;
import edu.java.domain.TgChat;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcTgChatService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcTgChatServiceTest extends IntegrationTest {
    @Autowired
    private JdbcTgChatService tgChatService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Rollback
    @Transactional
    void testRegisterNoExistChat() throws AlreadyRegisterException {
        tgChatService.register(444L);
        TgChat chat = jdbcTemplate.queryForObject(
            "select * from tg_chats where chat_id = ?",
            new BeanPropertyRowMapper<>(TgChat.class),
            444L
        );
        assertThat(chat.getId()).isGreaterThan(0);
        assertThat(chat.getChatId()).isEqualTo(444L);
    }

    @Test
    @Rollback
    @Transactional
    void testRegisterExistChat() {
        assertThrows(AlreadyRegisterException.class, () -> tgChatService.register(555555L));
    }

    @Test
    @Rollback
    @Transactional
    void testUnRegisterExistChat() throws ChatNotFoundException {
        tgChatService.unregister(555555L);
        assertThrows(
            EmptyResultDataAccessException.class,
            () -> jdbcTemplate.queryForObject("select * from tg_chats where chat_id = ?", TgChat.class, 555555L)
        );
    }

    @Test
    @Rollback
    @Transactional
    void testUnRegisterNoExistChat() {
        assertThrows(ChatNotFoundException.class, () -> tgChatService.unregister(999L));
    }

}
