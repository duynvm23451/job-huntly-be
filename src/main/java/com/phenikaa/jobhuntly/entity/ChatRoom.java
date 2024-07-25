package com.phenikaa.jobhuntly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_user_1_seen")
    private Boolean isUser1Seen;

    @Column(name = "is_user_2_seen")
    private Boolean isUser2Seen;

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    private User user2;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
