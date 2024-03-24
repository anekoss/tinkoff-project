package edu.java.configuration;

import edu.java.domain.LinkType;
import edu.java.service.LinkTypeService;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkTypeConfig {

    @Bean
    public Map<String, LinkType> linkTypeMap() {
        return Map.of(
            LinkType.STACKOVERFLOW.getHost(),
            LinkType.STACKOVERFLOW,
            LinkType.GITHUB.getHost(),
            LinkType.GITHUB
        );
    }

    @Bean
    public LinkTypeService linkTypeService() {
        return new LinkTypeService(linkTypeMap());
    }
}
