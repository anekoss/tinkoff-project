package edu.java.service.updateChecker;

import edu.java.client.StackOverflowClient;
import edu.java.client.dto.StackOverflowResponse;
import edu.java.client.exception.BadResponseBodyException;
import edu.java.domain.Link;
import edu.java.service.UpdateChecker;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class StackOverflowUpdateChecker implements UpdateChecker {
    private static final int PART_QUESTION = 4;
    private final StackOverflowClient stackOverflowClient;

    public Link check(Link link) {
        Long question = getQuestion(link.getUri().toString());
        if (question != -1) {
            try {
                StackOverflowResponse response = stackOverflowClient.fetchQuestion(question);
                if (response != null) {
                    OffsetDateTime updatedAt = link.getUpdatedAt();
                    for (StackOverflowResponse.StackOverflowItem item : response.items()) {
                        if (item.updatedAt().isAfter(updatedAt)) {
                            updatedAt = item.updatedAt();
                        }
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

    private Long getQuestion(String uri) {
        String[] pathParts = uri.split("/");
        if (pathParts.length >= PART_QUESTION) {
            return Long.parseLong(pathParts[PART_QUESTION]);
        }
        return -1L;
    }

}
