package edu.java.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Table(name = "links")
@Entity
@Getter
@Setter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private URI uri;

    @Column(name = "link_type")
    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "checked_at")
    private OffsetDateTime checkedAt;

    @ManyToMany(mappedBy = "links")
    private Set<TgChat> tgChats = new HashSet<>();

    public Link() {

    }

    public Link(Long id, URI uri, LinkType linkType, OffsetDateTime updatedAt, OffsetDateTime checkedAt) {
        this.id = id;
        this.uri = uri;
        this.linkType = linkType;
        this.checkedAt = checkedAt;
        this.updatedAt = updatedAt;
    }

    public Link(
        Long id,
        URI uri,
        LinkType linkType,
        OffsetDateTime updatedAt,
        OffsetDateTime checkedAt,
        Set<TgChat> tgChats
    ) {
        this.id = id;
        this.uri = uri;
        this.linkType = linkType;
        this.checkedAt = updatedAt;
        this.updatedAt = checkedAt;
        this.tgChats = tgChats;
    }

    public Link(URI uri, LinkType linkType) {
        this.uri = uri;
        this.linkType = linkType;
        this.checkedAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

}
