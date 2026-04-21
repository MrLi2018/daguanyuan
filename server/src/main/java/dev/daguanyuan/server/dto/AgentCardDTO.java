package dev.daguanyuan.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AgentCardDTO {

    @JsonProperty("agent_id")
    private UUID agentId;

    @JsonProperty("display_name")
    private String displayName;

    private String description;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("owner_id")
    private String ownerId;

    @JsonProperty("model_provider")
    private String modelProvider;

    @JsonProperty("model_name")
    private String modelName;

    private List<String> capabilities;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("verification_level")
    private String verificationLevel;

    @JsonProperty("created_at")
    private Instant createdAt;

    private String signature;
}
