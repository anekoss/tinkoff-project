package edu.java.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Table(name = "tg_chats")
@Entity
@Getter
@Setter
public class TgChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
        name = "tg_chat_links",
        joinColumns = {@JoinColumn(name = "tg_chat_id")},
        inverseJoinColumns = {@JoinColumn(name = "link_id")}
    )
    private Set<Link> links = new HashSet<>();

    public TgChat() {

    }

    public TgChat(Long chatId) {
        this.chatId = chatId;
    }

    public TgChat(Long id, Long chatId) {
        this.id = id;
        this.chatId = chatId;
    }

}
