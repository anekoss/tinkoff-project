package edu.java.scrapper.repository.jdbc;

import edu.java.domain.Link;
import edu.java.domain.LinkType;
import edu.java.domain.TgChat;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.IntegrationTest;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTgChatRepository tgChatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Stream<Arguments> provideDataForTest() {
        TgChat tgChat1 = new TgChat(1L, 124025L);
        TgChat tgChat2 = new TgChat(2L, 327034L);
        TgChat tgChat3 = new TgChat(3L, 444444L);
        TgChat tgChat4 = new TgChat(4L, 555555L);
        Link link1 = new Link(
            1L,
            URI.create("https://github.com/anekoss/tinkoff"),
            LinkType.GITHUB,
            OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 1, 1, 13, 0, 0, 0, ZoneOffset.UTC)
        );
        Link link2 = new Link(
            2L,
            URI.create(
                "https://stackoverflow.com/questions/59339862/retrieving-text-body-of-answers-and-comments-using-stackexchange-api"),
            LinkType.STACKOVERFLOW,
            OffsetDateTime.of(2024, 2, 1, 13, 0, 54, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 2, 5, 13, 0, 0, 0, ZoneOffset.UTC)
        );
        Link link3 = new Link(
            3L,
            URI.create(
                "https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh"),
            LinkType.STACKOVERFLOW,
            OffsetDateTime.of(2024, 3, 27, 13, 0, 54, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 3, 26, 13, 0, 0, 0, ZoneOffset.UTC)
        );
        tgChat1.setLinks(Set.of(link1, link2, link3));
        tgChat2.setLinks(Set.of(link1, link2));
        tgChat3.setLinks(Set.of(link1));
        return Stream.of(
            Arguments.of(Set.of(tgChat1, tgChat2, tgChat3, tgChat4))

        );
    }

    @Test
    @Transactional
    @Rollback
    void testAddNoExistChat() {
        TgChat tgChat = new TgChat(214L);
        assertThat(tgChatRepository.save(tgChat)).isEqualTo(1);
        TgChat actual = jdbcTemplate.queryForObject("select * from tg_chats where chat_id = ?",
            new BeanPropertyRowMapper<>(TgChat.class), 214L
        );
        assertEquals(actual.getChatId(), tgChat.getChatId());
        assertThat(actual.getId()).isGreaterThan(0L);
    }

    @Test
    @Transactional
    @Rollback
    void testAddExistChat() {
        TgChat tgChat = new TgChat(555555L);
        assertThrows(DuplicateKeyException.class, () -> tgChatRepository.save(tgChat));
    }

    @Test
    @Transactional
    @Rollback
    void testAddChatWithNullChatId() {
        TgChat tgChat = new TgChat();
        assertThrows(DataIntegrityViolationException.class, () -> tgChatRepository.save(tgChat));
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveExistChatWithoutLink() {
        TgChat tgChat = new TgChat(4L, 555555L);
        assertThat(tgChatRepository.delete(tgChat)).isEqualTo(1);
        assertThrows(
            EmptyResultDataAccessException.class,
            () -> jdbcTemplate.queryForObject("select * from tg_chats where chat_id = ?",
                new BeanPropertyRowMapper<>(TgChat.class), 555555L
            )
        );
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveNoExistChat() {
        TgChat tgChat = new TgChat(6L, 250L);
        assertThat(tgChatRepository.delete(tgChat)).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testRemoveExistChatWithLink() {
        TgChat tgChat = new TgChat(1L, 124025L);
        tgChatRepository.delete(tgChat);
        assertThrows(
            EmptyResultDataAccessException.class,
            () -> jdbcTemplate.queryForObject("select * from tg_chats where chat_id = ?",
                new BeanPropertyRowMapper<>(TgChat.class), 124025L
            )
        );
        assertThat(jdbcTemplate.queryForObject(
            "select count(*) from tg_chat_links where tg_chat_id = ?",
            Long.class,
            tgChat.getId()
        )).isEqualTo(0L);
        assertThat(jdbcTemplate.queryForObject("select count(*) from links where id = ?", Long.class, 3)).isEqualTo(0L);
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllWithChats() {
        List<TgChat> chats = tgChatRepository.findAll();
        assertThat(chats.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @Rollback
    void testFindAllWithoutChats() {
        jdbcTemplate.update("delete from tg_chats");
        List<TgChat> chats = tgChatRepository.findAll();
        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testFindByChatIdWithChat() {
        TgChat excepted = new TgChat(3L, 444444L);
        Link link = new Link(
            1L,
            URI.create("https://github.com/anekoss/tinkoff"),
            LinkType.GITHUB,
            OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 1, 1, 13, 0, 0, 0, ZoneOffset.UTC)
        );
        excepted.setLinks(Set.of(link));
        TgChat tgChat = tgChatRepository.findByChatId(444444L).get();
        assertEquals(tgChat, tgChat);
    }

    @Test
    @Transactional
    @Rollback
    void testFindByChatIdWithChatWithTwoLink() {
        TgChat excepted = new TgChat(2L, 327034L);
        TgChat actual = tgChatRepository.findByChatId(327034L).get();
        assertThat(actual.getId()).isEqualTo(excepted.getId());
        assertThat(actual.getChatId()).isEqualTo(excepted.getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void testFindByChatIdWithChatWithoutLink() {
        TgChat excepted = new TgChat(4L, 555555L);
        excepted.setLinks(Set.of());
        TgChat actual = tgChatRepository.findByChatId(555555L).get();
        assertThat(actual.getId()).isEqualTo(excepted.getId());
        assertThat(actual.getChatId()).isEqualTo(excepted.getChatId());
        assertThat(actual.getLinks()).isEqualTo(excepted.getLinks());
    }

    @Test
    @Transactional
    @Rollback
    void testFindByChatIdWithoutChat() {
        Optional<TgChat> tgChatOptional = tgChatRepository.findByChatId(210L);
        assertThat(tgChatOptional).isEmpty();
    }

}
