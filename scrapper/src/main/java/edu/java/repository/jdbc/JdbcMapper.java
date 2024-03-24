package edu.java.repository.jdbc;

import edu.java.domain.Link;
import edu.java.domain.LinkType;
import edu.java.domain.TgChat;
import java.net.URI;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcMapper {
    private JdbcMapper() {
    }

    static Set<TgChat> listMapToSetTgChat(List<Map<String, Object>> tgChatList) {
        return tgChatList.stream().map(m -> {
            TgChat chat = new TgChat((Long) m.get("chat_id"));
            chat.setId((Long) m.get("id"));
            return chat;
        }).collect(Collectors.toSet());
    }

    static Set<Link> listMapToSetLink(List<Map<String, Object>> linkList) {
        return linkList.stream().map(m -> {
            Link link = new Link(URI.create((String) m.get("uri")), LinkType.valueOf((String) m.get("link_type")));
            link.setUpdatedAt(((Timestamp) m.get("updated_at")).toInstant().atOffset(ZoneOffset.UTC));
            link.setCheckedAt(((Timestamp) m.get("checked_at")).toInstant().atOffset(ZoneOffset.UTC));
            link.setId((Long) m.get("id"));
            return link;
        }).collect(Collectors.toSet());
    }
}
