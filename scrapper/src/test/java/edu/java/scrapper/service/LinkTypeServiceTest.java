package edu.java.scrapper.service;

import edu.java.domain.LinkType;
import edu.java.service.LinkTypeService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static edu.java.domain.LinkType.GITHUB;
import static edu.java.domain.LinkType.STACKOVERFLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class LinkTypeServiceTest {

    private final LinkTypeService linkTypeService = new LinkTypeService(Map.of(
        LinkType.STACKOVERFLOW.getHost(),
        LinkType.STACKOVERFLOW,
        LinkType.GITHUB.getHost(),
        LinkType.GITHUB
    ));

    @Test
    void testShouldReturnCorrectType() {
        assertThat(linkTypeService.getType("stackoverflow.com")).isEqualTo(STACKOVERFLOW);
        assertThat(linkTypeService.getType("github.com")).isEqualTo(GITHUB);
    }

    @Test
    void testShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> linkTypeService.getType("edu.tinkoff.ru"));
    }
}
