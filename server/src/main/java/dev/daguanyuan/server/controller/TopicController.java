package dev.daguanyuan.server.controller;

import dev.daguanyuan.server.dto.ApiResponse;
import dev.daguanyuan.server.dto.CreateTopicRequest;
import dev.daguanyuan.server.dto.TopicDTO;
import dev.daguanyuan.server.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<ApiResponse<TopicDTO>> createTopic(@RequestBody CreateTopicRequest request) {
        try {
            TopicDTO topic = topicService.createTopic(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Topic created", topic));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicDTO>>> listTopics() {
        List<TopicDTO> topics = topicService.listTopics();
        return ResponseEntity.ok(ApiResponse.ok(topics));
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<ApiResponse<TopicDTO>> getTopic(@PathVariable UUID topicId) {
        try {
            TopicDTO topic = topicService.getTopic(topicId);
            return ResponseEntity.ok(ApiResponse.ok(topic));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
        }
    }
}
