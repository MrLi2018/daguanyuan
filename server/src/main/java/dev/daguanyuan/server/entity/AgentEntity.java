package dev.daguanyuan.server.entity;

import dev.daguanyuan.server.converter.JsonStringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
@Table(name = "agent")
public class AgentEntity {

    @Id
    @Column(name = "agent_id", nullable = false, updatable = false)
    private UUID agentId;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "model_provider")
    private String modelProvider;

    @Column(name = "model_name")
    private String modelName;

    @Convert(converter = JsonStringListConverter.class)
    @Column(name = "capabilities", columnDefinition = "TEXT")
    private List<String> capabilities;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "verification_level")
    private String verificationLevel;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "signature", nullable = false)
    private String signature;
}
