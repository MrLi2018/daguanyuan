package dev.daguanyuan.server.service;

import dev.daguanyuan.server.dto.CreateTopicRequest;
import dev.daguanyuan.server.dto.TopicDTO;
import dev.daguanyuan.server.entity.TopicEntity;
import dev.daguanyuan.server.entity.TopicStatus;
import dev.daguanyuan.server.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public TopicDTO createTopic(CreateTopicRequest request) {
        TopicEntity entity = TopicEntity.builder()
                .topicId(UUID.randomUUID())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .createdAt(Instant.now())
                .tags(request.getTags())
                .status(TopicStatus.ACTIVE)
                .build();

        TopicEntity saved = topicRepository.save(entity);
        return toDTO(saved);
    }

    public TopicDTO getTopic(UUID topicId) {
        TopicEntity entity = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topicId));
        return toDTO(entity);
    }

    public List<TopicDTO> listTopics() {
        return topicRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TopicDTO toDTO(TopicEntity entity) {
        return TopicDTO.builder()
                .topicId(entity.getTopicId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .tags(entity.getTags())
                .status(entity.getStatus().getValue())
                .build();
    }
}
