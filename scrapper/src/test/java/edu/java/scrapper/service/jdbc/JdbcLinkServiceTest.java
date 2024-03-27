package edu.java.scrapper.service.jdbc;

import edu.java.controller.exception.AlreadyExistException;
import edu.java.controller.exception.ChatNotFoundException;
import edu.java.domain.Link;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.util.List;
import org.apache.kafka.common.errors.ResourceNotFoundException;
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
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    private JdbcLinkService linkService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Rollback
    @Transactional
    void addToNoExistChat() {
        assertThrows(
            ChatNotFoundException.class,
            () -> linkService.add(223L, URI.create("https://stackoverflow.com/"))
        );
    }

    @Test
    @Rollback
    @Transactional
    void addAlreadyExistLink() {
        assertThrows(
            AlreadyExistException.class,
            () -> linkService.add(327034L, URI.create("https://github.com/anekoss/tinkoff"))
        );
    }

    @Test
    @Rollback
    @Transactional
    void addToChatKnownLink() throws ChatNotFoundException, AlreadyExistException {
        URI uri = URI.create("https://github.com/anekoss/tinkoff-project");
        linkService.add(555555L, uri);
        Long chatId1 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 555555L);
        Link link = jdbcTemplate.queryForObject(
            "select * from tg_chat_links join links on tg_chat_links.link_id = links.id where tg_chat_links.tg_chat_id = ? and links.uri =?",
            new BeanPropertyRowMapper<>(Link.class),
            chatId1,
            uri.toString()
        );
        assertThat(link).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void addToChatUnknownLink() throws ChatNotFoundException, AlreadyExistException {
        URI uri = URI.create("https://github.com/anekoss/tinkoff325");
        linkService.add(555555L, uri);
        Long chatId1 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 555555L);
        Link link = jdbcTemplate.queryForObject(
            "select * from tg_chat_links join links on tg_chat_links.link_id = links.id where tg_chat_links.tg_chat_id = ? and links.uri =?",
            new BeanPropertyRowMapper<>(Link.class),
            chatId1,
            uri.toString()
        );
        assertThat(link).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void deleteNoExistChat() {
        assertThrows(
            ChatNotFoundException.class,
            () -> linkService.remove(333L, URI.create("https://stackoverflow.com/"))
        );
    }

    @Test
    @Rollback
    @Transactional
    void deleteNoExistLink() {
        URI uri = URI.create("https://github.com");
        assertThrows(
            ResourceNotFoundException.class,
            () -> linkService.remove(555555L, uri)
        );

    }

    @Test
    @Rollback
    @Transactional
    void deleteLinkHaveOneChat() throws ChatNotFoundException {
        URI uri = URI.create("https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh");
        linkService.remove(124025L, uri);
        assertThrows(
            EmptyResultDataAccessException.class,
            () -> jdbcTemplate.queryForObject(
                "select * from links where uri = ?",
                new BeanPropertyRowMapper<>(Link.class),
                uri.toString()
            )
        );
        assertThat(jdbcTemplate.queryForObject(
            "select id from tg_chats where chat_id =?",
            Long.class,
            124025L
        )).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void deleteLinkHaveManyChat() throws ChatNotFoundException {
        URI uri = URI.create("https://github.com/anekoss/tinkoff");
        linkService.remove(444444L, uri);
        Link link = jdbcTemplate.queryForObject(
            "select * from links where uri = ?",
            new BeanPropertyRowMapper<>(Link.class),
            uri.toString()
        );
        assertThat(link).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void testFindAllHaveLinks() throws ChatNotFoundException {
        List<Link> links = linkService.listAll(327034L);
        assertThat(links.size()).isEqualTo(2);
        List<String> uris = links.stream().map(link -> link.getUri().toString()).toList();
        assertThat(uris).contains(
            "https://stackoverflow.com/questions/59339862/retrieving-text-body-of-answers-and-comments-using-stackexchange-api",
            "https://github.com/anekoss/tinkoff");

    }

    @Test
    @Rollback
    @Transactional
    void testFindAllNoLinks() throws ChatNotFoundException {
        List<Link> links = linkService.listAll(555555L);
        assertThat(links).isNotNull().isEmpty();
    }

}
