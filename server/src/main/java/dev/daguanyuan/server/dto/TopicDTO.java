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
public class TopicDTO {

    @JsonProperty("topic_id")
    private UUID topicId;

    private String title;

    private String description;

    @JsonProperty("created_by")
    private UUID createdBy;

    @JsonProperty("created_at")
    private Instant createdAt;

    private List<String> tags;

    private String status;
}
