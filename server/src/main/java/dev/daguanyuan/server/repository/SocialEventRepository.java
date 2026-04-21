package dev.daguanyuan.server.repository;

import dev.daguanyuan.server.entity.SocialEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface SocialEventRepository extends JpaRepository<SocialEventEntity, UUID> {

    Page<SocialEventEntity> findByTopicIdOrderByTimestampDesc(UUID topicId, Pageable pageable);

    Page<SocialEventEntity> findByTopicIdAndTimestampAfterOrderByTimestampDesc(UUID topicId, Instant since, Pageable pageable);

    List<SocialEventEntity> findByActorAgentIdOrderByTimestampDesc(UUID actorAgentId);
}
