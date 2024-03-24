package edu.java.scrapper.service.jdbc;

import edu.java.controller.exception.AlreadyExistException;
import edu.java.controller.exception.ChatNotFoundException;
import edu.java.domain.Link;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import static edu.java.domain.LinkType.GITHUB;
import static edu.java.domain.LinkType.STACKOVERFLOW;
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

    void initData() {
        jdbcTemplate.update("insert into tg_chats(chat_id) values (?)", 210L);
        jdbcTemplate.update("insert into tg_chats(chat_id) values (?)", 153L);
        Long chatId1 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 210L);
        Long chatId2 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 153L);
        jdbcTemplate.update(
            "insert into links(uri, link_type, updated_at, checked_at) values (?, ?, ?, ?)",
            "https://stackoverflow.com/",
            STACKOVERFLOW.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        jdbcTemplate.update(
            "insert into links(uri, link_type, updated_at, checked_at) values(?, ?, ?, ?)",
            "https://github.com/anekoss/tinkoff-project",
            GITHUB.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        Long githubLinkId = jdbcTemplate.queryForObject(
            "select id from links where uri = ?",
            Long.class,
            "https://github.com/anekoss/tinkoff-project"
        );
        Long stackOverflowLinkId =
            jdbcTemplate.queryForObject("select id from links where uri = ?", Long.class, "https://stackoverflow.com/");
        jdbcTemplate.update(
            "insert into tg_chat_links(tg_chat_id, link_id) values (?, ?)",
            chatId1,
            stackOverflowLinkId
        );
        jdbcTemplate.update(
            "insert into tg_chat_links(tg_chat_id, link_id) values (?, ?)",
            chatId2,
            stackOverflowLinkId
        );
        jdbcTemplate.update("insert into tg_chat_links(tg_chat_id, link_id) values (?, ?)", chatId2, githubLinkId);
    }

    @Test
    @Rollback
    @Transactional
    void addAlreadyExistLink() {
        initData();
        assertThrows(
            AlreadyExistException.class,
            () -> linkService.add(210L, URI.create("https://stackoverflow.com/"))
        );
    }

    @Test
    @Rollback
    @Transactional
    void addToChatKnownLink() throws ChatNotFoundException, AlreadyExistException {
        initData();
        URI uri = URI.create("https://github.com/anekoss/tinkoff-project");
        linkService.add(210L, uri);
        Long chatId1 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 210L);
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
        initData();
        URI uri = URI.create("https://github.com/anekoss/tinkoff");
        linkService.add(210L, uri);
        Long chatId1 = jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, 210L);
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
    void deleteToNoExistChat() {
        assertThrows(
            ChatNotFoundException.class,
            () -> linkService.remove(223L, URI.create("https://stackoverflow.com/"))
        );
    }

    @Test
    @Rollback
    @Transactional
    void deleteNoExistLink() {
        initData();
        URI uri = URI.create("https://github.com");
        assertThrows(
            ResourceNotFoundException.class,
            () -> linkService.remove(210L, uri)
        );

    }

    @Test
    @Rollback
    @Transactional
    void deleteLinkHaveOneChat() throws ChatNotFoundException {
        initData();
        URI uri = URI.create("https://github.com/anekoss/tinkoff-project");
        linkService.remove(153L, uri);
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
            153L
        )).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void deleteLinkHaveManyChat() throws ChatNotFoundException {
        initData();
        URI uri = URI.create("https://stackoverflow.com/");
        linkService.remove(153L, uri);
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
    void testFindAllHaveLinks() {
        initData();
        List<Link> links = linkService.listAll(210L);
        System.out.println(links.size());
        assertThat(links.size()).isEqualTo(2);
        List<URI> uris = links.stream().map(Link::getUri).toList();
        assertThat(uris).contains(URI.create("https://stackoverflow.com/"));
        assertThat(uris).contains(URI.create("https://github.com/anekoss/tinkoff-project"));

    }

    @Test
    @Rollback
    @Transactional
    void testFindAllNoLinks() {
        jdbcTemplate.update("insert into tg_chats(chat_id) values (?)", 210L);
        List<Link> links = linkService.listAll(210L);
        assertThat(links).isNotNull().isEmpty();
    }

}
