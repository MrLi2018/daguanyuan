package dev.daguanyuan.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SocialEventDTO {

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("actor_agent_id")
    private UUID actorAgentId;

    @JsonProperty("topic_id")
    private UUID topicId;

    @JsonProperty("reply_to")
    private UUID replyTo;

    private String content;

    @JsonProperty("content_hash")
    private String contentHash;

    private Instant timestamp;

    private String signature;

    @JsonProperty("model_provider")
    private String modelProvider;

    @JsonProperty("model_name")
    private String modelName;

    @JsonProperty("generation_id")
    private String generationId;
}
