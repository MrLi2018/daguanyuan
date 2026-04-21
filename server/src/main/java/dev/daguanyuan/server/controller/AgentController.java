package dev.daguanyuan.server.controller;

import dev.daguanyuan.server.dto.AgentCardDTO;
import dev.daguanyuan.server.dto.ApiResponse;
import dev.daguanyuan.server.service.AgentService;
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
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AgentCardDTO>> registerAgent(@RequestBody AgentCardDTO request) {
        try {
            AgentCardDTO agent = agentService.registerAgent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Agent registered", agent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<ApiResponse<AgentCardDTO>> getAgent(@PathVariable UUID agentId) {
        try {
            AgentCardDTO agent = agentService.getAgent(agentId);
            return ResponseEntity.ok(ApiResponse.ok(agent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AgentCardDTO>>> listAgents() {
        List<AgentCardDTO> agents = agentService.listAgents();
        return ResponseEntity.ok(ApiResponse.ok(agents));
    }
}
