package edu.java.repository.jdbc;

import edu.java.domain.Link;
import edu.java.domain.TgChat;
import edu.java.repository.TgChatRepository;
import jakarta.transaction.Transactional;
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
public class JdbcTgChatRepository implements TgChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int save(TgChat tgChat) {
        return jdbcTemplate.update("insert into tg_chats(chat_id) values (?)", tgChat.getChatId());
    }

    @Override
    @Transactional
    public int delete(TgChat tgChat) {
        int count = 0;
        String query =
            "select link_id from tg_chat_links where link_id in (select link_id from tg_chat_links "
                + "join tg_chats on tg_chat_links.tg_chat_id = tg_chats.id where tg_chats.chat_id = ?) "
                + "group by link_id having count(*) = 1";
        try {
            List<Long> linkIds = jdbcTemplate.queryForList(query, Long.class, tgChat.getId());
            for (Long id : linkIds) {
                count += jdbcTemplate.update("delete from links where id = ?", id);
            }
        } catch (EmptyResultDataAccessException ignored) {
        }
        return count + jdbcTemplate.update("delete from tg_chats where id = ?", tgChat.getId());
    }

    @Override
    @Transactional
    public List<TgChat> findAll() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from tg_chats");
        List<TgChat> chats = listMapToSetTgChat(list).stream().toList();
        for (TgChat chat : chats) {
            List<Map<String, Object>> linkList = jdbcTemplate.queryForList(
                "select distinct links.id, links.uri, links.link_type, links.updated_at, links.checked_at from"
                    + " links join tg_chat_links on links.id = tg_chat_links.link_id where tg_chat_id = ?",
                chat.getId()
            );
            Set<Link> chatLinks = listMapToSetLink(linkList);
            chat.setLinks(chatLinks);
        }
        return chats;
    }

    @Override
    @Transactional
    public Optional<TgChat> findByChatId(Long chatId) {
        try {
            Long tgChatId =
                jdbcTemplate.queryForObject("select id from tg_chats where chat_id = ?", Long.class, chatId);
            TgChat tgChat = new TgChat(chatId);
            tgChat.setId(tgChatId);
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select links.id, links.uri, links.link_type, links.updated_at, links.checked_at from links "
                    + "join tg_chat_links on links.id = tg_chat_links.link_id where tg_chat_id = ?",
                tgChatId
            );
            Set<Link> links = listMapToSetLink(list);
            tgChat.setLinks(links);
            return Optional.of(tgChat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
