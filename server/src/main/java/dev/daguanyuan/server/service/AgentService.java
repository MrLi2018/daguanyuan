package dev.daguanyuan.server.service;

import dev.daguanyuan.server.dto.AgentCardDTO;
import dev.daguanyuan.server.entity.AgentEntity;
import dev.daguanyuan.server.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;

    @Transactional
    public AgentCardDTO registerAgent(AgentCardDTO dto) {
        validateSignatureFormat(dto.getSignature());

        AgentEntity entity = AgentEntity.builder()
                .agentId(dto.getAgentId() != null ? dto.getAgentId() : UUID.randomUUID())
                .displayName(dto.getDisplayName())
                .description(dto.getDescription())
                .publicKey(dto.getPublicKey())
                .ownerId(dto.getOwnerId())
                .modelProvider(dto.getModelProvider())
                .modelName(dto.getModelName())
                .capabilities(dto.getCapabilities())
                .avatarUrl(dto.getAvatarUrl())
                .verificationLevel(dto.getVerificationLevel())
                .createdAt(Instant.now())
                .signature(dto.getSignature())
                .build();

        AgentEntity saved = agentRepository.save(entity);
        return toDTO(saved);
    }

    public AgentCardDTO getAgent(UUID agentId) {
        AgentEntity entity = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + agentId));
        return toDTO(entity);
    }

    public List<AgentCardDTO> listAgents() {
        return agentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AgentCardDTO toDTO(AgentEntity entity) {
        return AgentCardDTO.builder()
                .agentId(entity.getAgentId())
                .displayName(entity.getDisplayName())
                .description(entity.getDescription())
                .publicKey(entity.getPublicKey())
                .ownerId(entity.getOwnerId())
                .modelProvider(entity.getModelProvider())
                .modelName(entity.getModelName())
                .capabilities(entity.getCapabilities())
                .avatarUrl(entity.getAvatarUrl())
                .verificationLevel(entity.getVerificationLevel())
                .createdAt(entity.getCreatedAt())
                .signature(entity.getSignature())
                .build();
    }

    private void validateSignatureFormat(String signature) {
        if (signature == null || signature.isBlank()) {
            throw new IllegalArgumentException("Signature must not be empty");
        }
        try {
            Base64.getDecoder().decode(signature);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Signature must be valid base64 format");
        }
    }
}
