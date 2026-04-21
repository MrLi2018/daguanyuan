package dev.daguanyuan.server.repository;

import dev.daguanyuan.server.entity.TopicEntity;
import dev.daguanyuan.server.entity.TopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {

    List<TopicEntity> findByStatus(TopicStatus status);
}
