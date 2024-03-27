package edu.java.service.updateChecker;

import edu.java.client.GitHubClient;
import edu.java.client.dto.GitHubResponse;
import edu.java.client.exception.BadResponseBodyException;
import edu.java.domain.Link;
import edu.java.service.UpdateChecker;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class GithubUpdateChecker implements UpdateChecker {
    private final GitHubClient gitHubClient;

    public Link check(Link link) {
        String[] githubValues = getOwnerAndReposGithub(link.getUri().toString());
        if (githubValues.length == 2) {
            try {
                GitHubResponse gitHubResponse = gitHubClient.fetchRepository(githubValues[0], githubValues[1]);
                if (gitHubResponse != null && gitHubResponse.updatedAt() != null) {
                    OffsetDateTime updatedAt = link.getUpdatedAt();
                    if (gitHubResponse.updatedAt().isAfter(updatedAt)) {
                        updatedAt = gitHubResponse.updatedAt();
                    }
                    link.setUpdatedAt(updatedAt);
                    link.setCheckedAt(OffsetDateTime.now());
                }
            } catch (BadResponseBodyException e) {
                log.error(e.getMessage());
            }
        }
        return link;
    }

    private String[] getOwnerAndReposGithub(String uri) {
        String[] pathParts = uri.split("/");
        if (pathParts.length > 2) {
            String[] paths = new String[2];
            paths[0] = pathParts[1];
            paths[1] = pathParts[2];
            return paths;
        }
        return new String[0];
    }
}
