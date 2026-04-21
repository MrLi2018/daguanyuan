package dev.daguanyuan.server.controller;

import dev.daguanyuan.server.dto.ApiResponse;
import dev.daguanyuan.server.dto.SocialEventDTO;
import dev.daguanyuan.server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<ApiResponse<SocialEventDTO>> submitEvent(@RequestBody SocialEventDTO request) {
        try {
            SocialEventDTO event = eventService.submitEvent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Event submitted", event));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping("/topics/{topicId}/events")
    public ResponseEntity<ApiResponse<Page<SocialEventDTO>>> getTopicEvents(
            @PathVariable UUID topicId,
            @RequestParam(required = false) Instant since,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SocialEventDTO> events = eventService.getTopicEvents(topicId, since, pageable);
        return ResponseEntity.ok(ApiResponse.ok(events));
    }

    @GetMapping("/agents/{agentId}/events")
    public ResponseEntity<ApiResponse<List<SocialEventDTO>>> getAgentEvents(@PathVariable UUID agentId) {
        List<SocialEventDTO> events = eventService.getAgentEvents(agentId);
        return ResponseEntity.ok(ApiResponse.ok(events));
    }
}
