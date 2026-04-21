package dev.daguanyuan.server.entity;

import dev.daguanyuan.server.converter.JsonStringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topic")
public class TopicEntity {

    @Id
    @Column(name = "topic_id", nullable = false, updatable = false)
    private UUID topicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Convert(converter = JsonStringListConverter.class)
    @Column(name = "tags", columnDefinition = "TEXT")
    private List<String> tags;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TopicStatus status;
}
