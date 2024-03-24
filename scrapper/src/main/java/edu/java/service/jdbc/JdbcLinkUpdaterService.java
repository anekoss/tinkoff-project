package edu.java.service.jdbc;

import edu.java.client.BotClient;
import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.exception.BadResponseBodyException;
import edu.java.domain.Link;
import edu.java.domain.LinkType;
import edu.java.domain.TgChat;
import edu.java.repository.LinkRepository;
import edu.java.service.LinkUpdaterService;
import edu.java.service.UpdateChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final Map<LinkType, UpdateChecker> updateCheckerMap;
    private final BotClient botClient;
    private final Long limit;

    public List<Link> update() {
        List<Link> links = linkRepository.findStaleLinks(limit);
        List<Link> updates = new ArrayList<>();
        for (Link link : links) {
            Link updatedLink = updateCheckerMap.get(link.getLinkType()).check(link);
            if (updatedLink != null && updatedLink.getCheckedAt() != link.getCheckedAt()) {
                linkRepository.update(link.getId(), updatedLink.getUpdatedAt(), updatedLink.getCheckedAt());
                if (updatedLink.getUpdatedAt().isAfter(link.getUpdatedAt())) {
                    updates.add(updatedLink);
                }
            }
        }
        return updates;
    }

    public long sendUpdates(List<Link> links) {
        long countUpdate = 0;
        for (Link link : links) {
            try {
                long[] chatIds = link.getTgChats().stream().mapToLong(TgChat::getChatId).toArray();
                if (chatIds != null && chatIds.length != 0) {
                    botClient.linkUpdates(new LinkUpdateRequest(1L, link.getUri().toString(), "updated", chatIds));
                    countUpdate++;
                }
            } catch (BadResponseBodyException e) {
                log.error(e.getMessage());
            }
        }
        return countUpdate;
    }
}
