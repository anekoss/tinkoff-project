package edu.java.repository;

import edu.java.domain.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    int save(Long tgChatId, Link link);

    int delete(Long tgChatId, URI uri);

    List<Link> findAll();

    Optional<Long> findIdByUri(URI uri);

    List<Link> findStaleLinks(Long limit);

    int update(Long linkId, OffsetDateTime updatedAt, OffsetDateTime checkedAt);

}
