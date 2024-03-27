package edu.java.service;

import edu.java.domain.LinkType;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LinkTypeService {
    private final Map<String, LinkType> linkTypeMap;

    public LinkTypeService(Map<String, LinkType> linkTypeMap) {
        this.linkTypeMap = linkTypeMap;
    }

    public LinkType getType(@NotBlank String host) {
        LinkType type = linkTypeMap.get(host);
        if (type == null) {
            throw new IllegalArgumentException();
        }
        return type;
    }

}

