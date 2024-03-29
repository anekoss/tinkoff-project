package edu.java.bot.service;

import edu.java.bot.commands.CommandExecutionStatus;
import edu.java.bot.domain.Link;
import edu.java.bot.domain.User;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.java.bot.commands.CommandExecutionStatus.USER_ALREADY_REGISTER;

@Service
@RequiredArgsConstructor
public class CommandService {
    private final UserService userService;
    private final RepositoryService repositoryService;
    private final LinkService linkService;

    public CommandExecutionStatus start(Long id) {
        User user = userService.createUser(id);
        return repositoryService.saveUser(id, user) ? CommandExecutionStatus.SUCCESS : USER_ALREADY_REGISTER;
    }

    public CommandExecutionStatus track(Long id, String url) {
        Optional<Link> link = linkService.createValidLink(url);
        return link.map(value -> repositoryService.startTrackLink(id, value.url(), value)
                ? CommandExecutionStatus.SUCCESS : CommandExecutionStatus.LINK_ALREADY_TRACK)
            .orElse(CommandExecutionStatus.LINK_INVALID);
    }

    public CommandExecutionStatus unTrack(Long id, String url) {
        Optional<Link> link = linkService.createValidLink(url);
        return link.map(value -> repositoryService.stopTrackLink(id, value.url()) ? CommandExecutionStatus.SUCCESS
            : CommandExecutionStatus.LINK_NOT_TRACK).orElse(CommandExecutionStatus.LINK_INVALID);
    }

    public Set<URL> list(Long id) {
        return repositoryService.getTrackLinks(id);
    }

    public boolean stop(Long id) {
        return repositoryService.removeUser(id);
    }

}
