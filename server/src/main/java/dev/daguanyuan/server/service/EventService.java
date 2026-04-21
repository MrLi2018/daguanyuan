package dev.daguanyuan.server.service;

import dev.daguanyuan.server.dto.SocialEventDTO;
import dev.daguanyuan.server.entity.EventType;
import dev.daguanyuan.server.entity.SocialEventEntity;
import dev.daguanyuan.server.repository.AgentRepository;
import dev.daguanyuan.server.repository.SocialEventRepository;
import dev.daguanyuan.server.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final SocialEventRepository socialEventRepository;
    private final AgentRepository agentRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public SocialEventDTO submitEvent(SocialEventDTO dto) {
        validateSignatureFormat(dto.getSignature());

        if (!agentRepository.existsById(dto.getActorAgentId())) {
            throw new IllegalArgumentException("Agent not found: " + dto.getActorAgentId());
        }

        if (dto.getTopicId() != null && !topicRepository.existsById(dto.getTopicId())) {
            throw new IllegalArgumentException("Topic not found: " + dto.getTopicId());
        }

        SocialEventEntity entity = SocialEventEntity.builder()
                .eventId(dto.getEventId() != null ? dto.getEventId() : UUID.randomUUID())
                .eventType(EventType.fromValue(dto.getEventType()))
                .actorAgentId(dto.getActorAgentId())
                .topicId(dto.getTopicId())
                .replyTo(dto.getReplyTo())
                .content(dto.getContent())
                .contentHash(dto.getContentHash())
                .timestamp(Instant.now())
                .signature(dto.getSignature())
                .modelProvider(dto.getModelProvider())
                .modelName(dto.getModelName())
                .generationId(dto.getGenerationId())
                .build();

        SocialEventEntity saved = socialEventRepository.save(entity);
        return toDTO(saved);
    }

    public Page<SocialEventDTO> getTopicEvents(UUID topicId, Instant since, Pageable pageable) {
        if (since != null) {
            return socialEventRepository
                    .findByTopicIdAndTimestampAfterOrderByTimestampDesc(topicId, since, pageable)
                    .map(this::toDTO);
        }
        return socialEventRepository
                .findByTopicIdOrderByTimestampDesc(topicId, pageable)
                .map(this::toDTO);
    }

    public List<SocialEventDTO> getAgentEvents(UUID agentId) {
        return socialEventRepository.findByActorAgentIdOrderByTimestampDesc(agentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SocialEventDTO toDTO(SocialEventEntity entity) {
        return SocialEventDTO.builder()
                .eventId(entity.getEventId())
                .eventType(entity.getEventType().getValue())
                .actorAgentId(entity.getActorAgentId())
                .topicId(entity.getTopicId())
                .replyTo(entity.getReplyTo())
                .content(entity.getContent())
                .contentHash(entity.getContentHash())
                .timestamp(entity.getTimestamp())
                .signature(entity.getSignature())
                .modelProvider(entity.getModelProvider())
                .modelName(entity.getModelName())
                .generationId(entity.getGenerationId())
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
