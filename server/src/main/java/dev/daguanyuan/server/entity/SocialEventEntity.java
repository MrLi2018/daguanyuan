package dev.daguanyuan.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "social_event")
public class SocialEventEntity {

    @Id
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "actor_agent_id", nullable = false)
    private UUID actorAgentId;

    @Column(name = "topic_id")
    private UUID topicId;

    @Column(name = "reply_to")
    private UUID replyTo;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "signature", nullable = false)
    private String signature;

    @Column(name = "model_provider")
    private String modelProvider;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "generation_id")
    private String generationId;
}
