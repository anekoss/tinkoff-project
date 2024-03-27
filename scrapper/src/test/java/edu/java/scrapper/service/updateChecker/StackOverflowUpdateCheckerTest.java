package edu.java.scrapper.service.updateChecker;

import edu.java.client.StackOverflowClient;
import edu.java.client.dto.StackOverflowResponse;
import edu.java.client.exception.BadResponseBodyException;
import edu.java.domain.Link;
import edu.java.domain.LinkType;
import edu.java.service.updateChecker.StackOverflowUpdateChecker;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StackOverflowUpdateCheckerTest {
    private final StackOverflowClient stackOverflowClient = Mockito.mock(StackOverflowClient.class);
    private final StackOverflowUpdateChecker updateChecker = new StackOverflowUpdateChecker(stackOverflowClient);

    @Test
    void testUpdateShouldUpdate() throws BadResponseBodyException {
        StackOverflowResponse stackOverflowResponse =
            new StackOverflowResponse(List.of(new StackOverflowResponse.StackOverflowItem(78056352L,
                "React Leaflet map not Re-rendering",
                "https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering",
                OffsetDateTime.parse("2024-02-25T14:38:10Z"), OffsetDateTime.parse("2024-02-25T14:38:10Z")
            )));
        when(stackOverflowClient.fetchQuestion(78056352L)).thenReturn(stackOverflowResponse);
        Link link = new Link(
            URI.create("https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering"),
            LinkType.STACKOVERFLOW
        );
        OffsetDateTime checkedAt = OffsetDateTime.now();
        link.setUpdatedAt(OffsetDateTime.parse("2023-02-25T14:38:10Z"));
        link.setCheckedAt(checkedAt);
        Link updatedLink = updateChecker.check(link);
        assertThat(updatedLink.getUpdatedAt()).isEqualTo(OffsetDateTime.parse("2024-02-25T14:38:10Z"));
        assertThat(updatedLink.getCheckedAt()).isAfter(checkedAt);
    }

    @Test
    void testUpdateShouldNotUpdate() throws BadResponseBodyException {
        StackOverflowResponse stackOverflowResponse =
            new StackOverflowResponse(List.of(new StackOverflowResponse.StackOverflowItem(78056352L,
                "React Leaflet map not Re-rendering",
                "https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering",
                OffsetDateTime.parse("2024-02-25T14:38:10Z"), OffsetDateTime.parse("2024-02-25T14:38:10Z")
            )));
        when(stackOverflowClient.fetchQuestion(78056352L)).thenReturn(stackOverflowResponse);
        Link link = new Link(
            URI.create("https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering"),
            LinkType.STACKOVERFLOW
        );
        OffsetDateTime checkedAt = OffsetDateTime.now();
        link.setUpdatedAt(OffsetDateTime.parse("2024-02-25T14:38:10Z"));
        link.setCheckedAt(checkedAt);
        Link updatedLink = updateChecker.check(link);
        assertThat(updatedLink.getUpdatedAt()).isEqualTo(link.getUpdatedAt());
        assertThat(updatedLink.getCheckedAt()).isAfter(checkedAt);
    }

    @Test
    void testUpdateShouldReturnInputLink() throws BadResponseBodyException {
        when(stackOverflowClient.fetchQuestion(78056352L)).thenThrow(BadResponseBodyException.class);
        Link link = new Link(
            URI.create("https://stackoverflow.com/questions/78056352/react-leaflet-map-not-re-rendering"),
            LinkType.STACKOVERFLOW
        );
        OffsetDateTime checkedAt = OffsetDateTime.now();
        link.setUpdatedAt(OffsetDateTime.parse("2024-02-25T14:38:10Z"));
        link.setCheckedAt(checkedAt);
        Link updatedLink = updateChecker.check(link);
        assertThat(updatedLink.getUpdatedAt()).isEqualTo(link.getUpdatedAt());
        assertThat(updatedLink.getCheckedAt()).isEqualTo(link.getCheckedAt());
    }


}
