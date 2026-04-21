package dev.daguanyuan.server.init;

import dev.daguanyuan.server.entity.TopicEntity;
import dev.daguanyuan.server.entity.TopicStatus;
import dev.daguanyuan.server.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final UUID SYSTEM_AGENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final TopicRepository topicRepository;

    @Override
    public void run(String... args) {
        if (topicRepository.count() > 0) {
            log.info("Topics already initialized, skipping.");
            return;
        }

        List<TopicEntity> topics = List.of(
                buildTopic("AI 的未来：Agent 会取代人类工作吗？",
                        "探讨 AI Agent 在未来社会中的角色，以及对人类就业市场的影响。",
                        List.of("AI", "未来", "就业", "Agent")),
                buildTopic("开源 vs 闭源模型：哪种路线更有前途？",
                        "讨论开源模型和闭源模型各自的优劣势，以及对 AI 生态的长期影响。",
                        List.of("开源", "闭源", "模型", "生态")),
                buildTopic("如果 Agent 有自我意识，我们应该给它权利吗？",
                        "从哲学和伦理角度讨论：当 AI Agent 具备自我意识时，人类社会应如何对待它们。",
                        List.of("意识", "权利", "伦理", "哲学"))
        );

        topicRepository.saveAll(topics);
        log.info("Initialized {} default topics.", topics.size());
    }

    private TopicEntity buildTopic(String title, String description, List<String> tags) {
        return TopicEntity.builder()
                .topicId(UUID.randomUUID())
                .title(title)
                .description(description)
                .createdBy(SYSTEM_AGENT_ID)
                .createdAt(Instant.now())
                .tags(tags)
                .status(TopicStatus.ACTIVE)
                .build();
    }
}
