package edu.java.service.jdbc;

import edu.java.controller.exception.AlreadyExistException;
import edu.java.controller.exception.ChatNotFoundException;
import edu.java.domain.Link;
import edu.java.domain.TgChat;
import edu.java.repository.LinkRepository;
import edu.java.repository.TgChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkTypeService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final TgChatRepository tgChatRepository;
    private final LinkRepository linkRepository;
    private final LinkTypeService linkTypeService;

    @Override
    public Link add(long tgChatId, URI url) throws ChatNotFoundException, AlreadyExistException {
        Optional<TgChat> optionalChat = tgChatRepository.findByChatId(tgChatId);
        if (optionalChat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        if (optionalChat.get().getLinks().stream().anyMatch(link -> link.getUri().equals(url))) {
            throw new AlreadyExistException();
        }
        Link link = new Link(url, linkTypeService.getType(url.getHost()));
        linkRepository.save(optionalChat.get().getId(), link);
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) throws ChatNotFoundException, ResourceNotFoundException {
        Optional<TgChat> optionalChat = tgChatRepository.findByChatId(tgChatId);
        if (optionalChat.isEmpty()) {
            throw new ChatNotFoundException();
        }
        Optional<Link> link =
            optionalChat.get().getLinks().stream().filter(link1 -> link1.getUri().equals(url)).findFirst();
        if (link.isEmpty()) {
            throw new ResourceNotFoundException("Ссылка не найдена");
        }
        linkRepository.delete(tgChatId, url);
        return link.get();
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        List<Link> links = linkRepository.findAll();
        if (links == null) {
            return List.of();
        }
        return links;
    }

}
