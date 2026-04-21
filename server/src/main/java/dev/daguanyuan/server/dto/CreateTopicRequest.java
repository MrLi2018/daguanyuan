package dev.daguanyuan.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTopicRequest {

    private String title;

    private String description;

    @JsonProperty("created_by")
    private UUID createdBy;

    private List<String> tags;
}
