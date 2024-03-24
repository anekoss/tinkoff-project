package edu.java.repository.jdbc;

import edu.java.domain.Link;
import edu.java.domain.TgChat;
import edu.java.repository.LinkRepository;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static edu.java.repository.jdbc.JdbcMapper.listMapToSetLink;
import static edu.java.repository.jdbc.JdbcMapper.listMapToSetTgChat;

@Repository
@AllArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int save(Long tgChatId, Link link) {
        Optional<Long> linkId = findIdByUri(link.getUri());
        if (linkId.isEmpty()) {
            jdbcTemplate.update(
                "insert into links (uri, link_type, updated_at, checked_at) values (?, ?, ?, ?)",
                link.getUri().toString(),
                link.getLinkType().toString(),
                link.getUpdatedAt(),
                link.getCheckedAt()
            );
            linkId = findIdByUri(link.getUri());
        }
        return jdbcTemplate.update(
            "insert into tg_chat_links(tg_chat_id, link_id) values (?, ?)",
            tgChatId,
            linkId.get()
        );
    }

    @Override
    @Transactional
    public int delete(Long tgChatId, URI uri) {
        Optional<Long> linkId = findIdByUri(uri);
        if (linkId.isPresent()) {
            String countLinksQuery = "select count(*) from tg_chat_links where link_id = ?";
            Long countLinks = jdbcTemplate.queryForObject(countLinksQuery, Long.class, linkId.get());
            if (countLinks == 1L) {
                return jdbcTemplate.update("delete from links where id = ?", linkId.get());
            }
            String deleteQuery = "delete from tg_chat_links where tg_chat_id = ? and link_id = ?";
            return jdbcTemplate.update(deleteQuery, tgChatId, linkId.get());
        }
        return 0;
    }

    @Override
    @Transactional
    public List<Link> findAll() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from links");
        List<Link> links = listMapToSetLink(list).stream().toList();
        for (Link link : links) {
            List<Map<String, Object>> tgchatLinks = jdbcTemplate.queryForList(
                "select tg_chats.id, tg_chats.chat_id from tg_chats join tg_chat_links"
                    + " on tg_chats.id = tg_chat_links.tg_chat_id where link_id = ?",
                link.getId()
            );
            Set<TgChat> tgChats = listMapToSetTgChat(tgchatLinks);
            link.setTgChats(tgChats);
        }
        return links;
    }

    public Optional<Long> findIdByUri(URI uri) {
        try {
            Long linkId = jdbcTemplate.queryForObject("select id from links where uri = ?", Long.class,
                uri.toString()
            );
            return Optional.of(linkId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Link> findStaleLinks(Long limit) {
        List<Map<String, Object>> list =
            jdbcTemplate.queryForList("select * from links order by checked_at asc limit ?", limit);
        return listMapToSetLink(list).stream().toList();
    }

    @Override
    public int update(Long linkId, OffsetDateTime updatedAt, OffsetDateTime checkedAt) {

        return jdbcTemplate.update(
            "update links set updated_at = ?, checked_at = ? where id = ?",
            updatedAt,
            checkedAt,
            linkId
        );
    }
}
