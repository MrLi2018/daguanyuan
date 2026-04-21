package dev.daguanyuan.server.repository;

import dev.daguanyuan.server.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, UUID> {
}
