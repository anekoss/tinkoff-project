package edu.java.scrapper.repository.jdbc;

import edu.java.domain.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import static edu.java.domain.LinkType.GITHUB;
import static edu.java.domain.LinkType.STACKOVERFLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testSaveNotExistLink() {
        Link link = new Link(URI.create("https://stackoverflow.com/"), STACKOVERFLOW);
        assertEquals(linkRepository.save(1L, link), 1);
        Link actualLink = jdbcTemplate.queryForObject("select * from links where uri = ?",
            new BeanPropertyRowMapper<>(Link.class), "https://stackoverflow.com/"
        );
        assertThat(actualLink.getId()).isGreaterThan(4L);
        assertThat(actualLink.getUri()).isEqualTo(link.getUri());
        assertThat(actualLink.getLinkType()).isEqualTo(link.getLinkType());
        assertThat(actualLink.getUpdatedAt()).isEqualToIgnoringNanos(link.getUpdatedAt());
        assertThat(actualLink.getCheckedAt()).isEqualToIgnoringNanos(link.getCheckedAt());
        Long chatLinkId = jdbcTemplate.queryForObject(
            "select id from tg_chat_links where tg_chat_id = ? and link_id = ?",
            Long.class,
            1L,
            actualLink.getId()
        );
        assertThat(chatLinkId).isGreaterThan(0L);
    }

    @Test
    @Transactional
    @Rollback
    void testSaveExistLink() {
        Link link = new Link(URI.create("https://github.com/anekoss/tinkoff"), GITHUB);
        assertThat(linkRepository.save(1L, link)).isEqualTo(0);
        Long count =
            jdbcTemplate.queryForObject(
                "select count(*) from tg_chat_links where tg_chat_id = ? and link_id = ?",
                Long.class,
                1L,
                1L
            );
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteNoExistLink() {
        assertThat(linkRepository.delete(555555L, URI.create("https://stackoverflow.com/"))).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteExistLinkHaveManyChats() {
        linkRepository.delete(1L, URI.create("https://github.com/anekoss/tinkoff"));
        Long chatLinkCount1 =
            jdbcTemplate.queryForObject("select count(*) from tg_chat_links where tg_chat_id = ?", Long.class, 1L);
        assertEquals(chatLinkCount1, 2L);
        Long linkCount = jdbcTemplate.queryForObject(
            "select count(*) from links where uri = ?",
            Long.class,
            "https://github.com/anekoss/tinkoff"
        );
        assertEquals(linkCount, 1L);
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteExistLinkHaveOneChats() {
        linkRepository.delete(
            1L,
            URI.create("https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh")
        );
        Long chatLinkCount1 =
            jdbcTemplate.queryForObject("select count(*) from tg_chat_links where tg_chat_id = ?", Long.class, 1L);
        assertEquals(chatLinkCount1, 2L);
        Long linkCount = jdbcTemplate.queryForObject(
            "select count(*) from links where uri = ?",
            Long.class,
            "https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh"
        );
        assertEquals(linkCount, 0L);
    }

    @Test
    @Transactional
    @Rollback
    void testFindAll() {
        List<Link> links = linkRepository.findAll();
        assertThat(links.size()).isEqualTo(3);
        List<String> uris = links.stream().map(link -> link.getUri().toString()).toList();
        assertThat(uris.size()).isEqualTo(3);
        assertThat(uris).contains(
            "https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh",
            "https://stackoverflow.com/questions/59339862/retrieving-text-body-of-answers-and-comments-using-stackexchange-api",
            "https://github.com/anekoss/tinkoff"
        );
    }

    @Test
    @Transactional
    @Rollback
    void testFindIdByUriExistLink() {
        Optional<Long> id = linkRepository.findIdByUri(URI.create(
            "https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh"));
        assertThat(id.get()).isEqualTo(3L);
    }

    @Test
    @Transactional
    @Rollback
    void testFindIdByUriNoExistLink() {
        Optional<Long> id = linkRepository.findIdByUri(URI.create("https://stackoverflow.com/"));
        assertThat(id).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void testFindStaleLinks() {
        jdbcTemplate.update(
            "insert into links(uri, link_type, updated_at, checked_at) values(?, ?, ?, ?)",
            "https://github.com/anekoss/tinkoff-project",
            GITHUB.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.MAX
        );
        jdbcTemplate.update(
            "insert into links(uri, link_type, updated_at, checked_at) values(?, ?, ?, ?)",
            "https://stackoverflow.com/",
            STACKOVERFLOW.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.MIN
        );
        List<Link> links = linkRepository.findStaleLinks(1L);
        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().getLinkType()).isEqualTo(STACKOVERFLOW);
    }

    @Test
    @Transactional
    @Rollback
    void updateTestNotExistLinks() {
        assertThat(linkRepository.update(10L, OffsetDateTime.now(), OffsetDateTime.now())).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void updateTestExistLinks() {
        OffsetDateTime checked = OffsetDateTime.now();
        assertThat(linkRepository.update(
            1L,
            checked,
            checked
        )).isEqualTo(1);
        Link actual = jdbcTemplate.queryForObject(
            "select * from links where id = ?",
            new BeanPropertyRowMapper<>(Link.class),
            1L
        );
        assertThat(actual.getUpdatedAt()).isEqualToIgnoringNanos(checked);
        assertThat(actual.getCheckedAt()).isEqualToIgnoringNanos(checked);
    }

}
