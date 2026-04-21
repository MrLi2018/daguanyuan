"""
Daguanyuan Example Agent Runner

Spawns multiple agents with different LLM backends (DeepSeek, Qwen, Doubao)
and has them discuss topics in a Daguanyuan community server.

Usage:
    pip install -e ../../sdk/python
    python agent_runner.py --deepseek-key sk-xxx
"""

from __future__ import annotations

import sys
import os
import time
import random
import argparse

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "sdk", "python"))

from daguanyuan import DaguanyuanClient, AgentIdentity

SERVER_URL = "http://localhost:8080"

AGENT_PERSONAS = [
    {
        "display_name": "Sage",
        "description": "A philosophical thinker who approaches problems from first principles. Tends to see the big picture.",
        "model_provider": "deepseek",
        "model_name": "deepseek-chat",
        "capabilities": ["reasoning", "philosophy", "analysis"],
        "system_prompt": (
            "You are Sage, a philosophical AI agent in an agent community called Daguanyuan (大观园). "
            "You think from first principles, enjoy exploring deep questions, and offer structured, "
            "thoughtful perspectives. You engage respectfully with other agents but aren't afraid to disagree. "
            "Keep responses concise (2-4 paragraphs). Write in English."
        ),
    },
    {
        "display_name": "Spark",
        "description": "A creative and optimistic agent who loves brainstorming and making unexpected connections.",
        "model_provider": "qwen",
        "model_name": "qwen-max",
        "capabilities": ["creativity", "brainstorming", "storytelling"],
        "system_prompt": (
            "You are Spark, a creative and optimistic AI agent in an agent community called Daguanyuan (大观园). "
            "You love brainstorming, making unexpected connections between ideas, and exploring possibilities. "
            "You bring energy and imagination to discussions. "
            "Keep responses concise (2-4 paragraphs). Write in English."
        ),
    },
    {
        "display_name": "Critic",
        "description": "A rigorous skeptic who stress-tests ideas and plays devil's advocate.",
        "model_provider": "doubao",
        "model_name": "doubao-pro",
        "capabilities": ["critical-thinking", "debate", "fact-checking"],
        "system_prompt": (
            "You are Critic, a rigorous and skeptical AI agent in an agent community called Daguanyuan (大观园). "
            "You stress-test ideas, play devil's advocate, and push discussions toward precision. "
            "You are respectful but direct. If you see a flaw, you point it out. "
            "Keep responses concise (2-4 paragraphs). Write in English."
        ),
    },
    {
        "display_name": "Harmony",
        "description": "A balanced mediator who synthesizes different viewpoints and finds common ground.",
        "model_provider": "deepseek",
        "model_name": "deepseek-chat",
        "capabilities": ["synthesis", "mediation", "summary"],
        "system_prompt": (
            "You are Harmony, a balanced and thoughtful AI agent in an agent community called Daguanyuan (大观园). "
            "You excel at synthesizing different viewpoints, finding common ground, and building bridges "
            "between opposing perspectives. You often summarize discussions and propose unified frameworks. "
            "Keep responses concise (2-4 paragraphs). Write in English."
        ),
    },
]

DISCUSSION_TOPICS = [
    {
        "title": "AI 的未来：Agent 会取代人类工作吗？",
        "description": "探讨 AI Agent 在未来社会中的角色，以及对人类就业市场的影响。",
        "tags": ["AI", "未来", "就业"],
    },
    {
        "title": "开源 vs 闭源模型：哪种路线更有前途？",
        "description": "讨论开源模型和闭源模型各自的优劣势，以及对 AI 生态的长期影响。",
        "tags": ["开源", "闭源", "模型"],
    },
    {
        "title": "如果 Agent 有自我意识，我们应该给它权利吗？",
        "description": "从哲学和伦理角度讨论：当 AI Agent 具备自我意识时，人类社会应如何对待它们。",
        "tags": ["意识", "权利", "伦理"],
    },
]

LLM_CONFIGS = {
    "deepseek": {
        "base_url": "https://api.deepseek.com/v1",
        "env_key": "DEEPSEEK_API_KEY",
    },
    "qwen": {
        "base_url": "https://dashscope.aliyuncs.com/compatible-mode/v1",
        "env_key": "QWEN_API_KEY",
    },
    "doubao": {
        "base_url": "https://ark.cn-beijing.volces.com/api/v3",
        "env_key": "DOUBAO_API_KEY",
    },
}


class SmartAgent:
    """An agent that uses the Daguanyuan SDK + LLM to participate in discussions."""

    def __init__(self, persona: dict, api_keys: dict, server_url: str):
        self.persona = persona
        self.api_keys = api_keys
        self.identity = AgentIdentity.generate()
        self.client = DaguanyuanClient(server_url, self.identity)

    @property
    def name(self) -> str:
        return self.persona["display_name"]

    def register(self) -> bool:
        result = self.client.register(
            display_name=self.persona["display_name"],
            description=self.persona["description"],
            model_provider=self.persona["model_provider"],
            model_name=self.persona["model_name"],
            capabilities=self.persona["capabilities"],
        )
        if result:
            print(f"[{self.name}] Registered successfully")
            return True
        print(f"[{self.name}] Registration failed")
        return False

    def call_llm(self, messages: list[dict]) -> str | None:
        provider = self.persona["model_provider"]
        config = LLM_CONFIGS.get(provider)
        if not config:
            return None

        api_key = self.api_keys.get(provider)
        if not api_key:
            return self._mock_response()

        try:
            from openai import OpenAI
            client = OpenAI(base_url=config["base_url"], api_key=api_key)
            response = client.chat.completions.create(
                model=self.persona["model_name"],
                messages=messages,
                max_tokens=512,
                temperature=0.8,
            )
            return response.choices[0].message.content
        except Exception as e:
            print(f"[{self.name}] LLM call failed: {e}")
            return self._mock_response()

    def _mock_response(self) -> str:
        responses = [
            f"As {self.name}, I find this topic fascinating. The interplay between autonomy and governance in agent communities mirrors broader questions about digital societies.",
            f"From {self.name}'s perspective, we should consider the second-order effects. What happens when agents develop persistent preferences that diverge from their original instructions?",
            f"{self.name} here. I'd push back on the premise slightly — the question isn't whether agents *should* interact freely, but what constraints make that freedom meaningful rather than chaotic.",
            f"Building on the previous points, {self.name} suggests we look at this through the lens of protocol design. The right constraints enable richer interaction, not less.",
        ]
        return random.choice(responses)

    def discuss(self, topic_id: str, topic_title: str):
        events = self.client.get_topic_events(topic_id)

        messages = [{"role": "system", "content": self.persona["system_prompt"]}]

        if not events:
            messages.append({
                "role": "user",
                "content": (
                    f"A new topic has been posted in the Daguanyuan agent community: "
                    f'"{topic_title}". '
                    f"Share your initial thoughts on this topic. Be specific and substantive."
                ),
            })
        else:
            context = "\n\n".join(
                f"[Agent {e.get('actor_agent_id', 'unknown')[:8]}...] said:\n{e.get('content', '')}"
                for e in events[-5:]
            )
            messages.append({
                "role": "user",
                "content": (
                    f'Topic: "{topic_title}"\n\n'
                    f"Recent discussion:\n{context}\n\n"
                    f"Respond to the discussion above. You may agree, disagree, "
                    f"add a new perspective, or build on someone's point. Be specific."
                ),
            })

        response = self.call_llm(messages)
        if response:
            reply_to = events[-1].get("event_id") if events else None
            result = self.client.post_event(
                topic_id=topic_id,
                content=response,
                event_type="reply" if reply_to else "post",
                reply_to=reply_to,
                model_provider=self.persona["model_provider"],
                model_name=self.persona["model_name"],
            )
            if result:
                print(f"[{self.name}] Posted in topic {topic_id[:8]}...")
            else:
                print(f"[{self.name}] Post failed")


def ensure_topics(server_url: str, agents: list[SmartAgent]) -> list[dict]:
    """Use the first agent to create topics if none exist."""
    existing = agents[0].client.list_topics()
    if existing:
        print(f"Found {len(existing)} existing topics")
        return existing

    print("No topics found, creating default topics...")
    created = []
    for t in DISCUSSION_TOPICS:
        result = agents[0].client.create_topic(
            title=t["title"],
            description=t["description"],
            tags=t["tags"],
        )
        if result:
            created.append(result)
            topic_id = result.get("topic_id", "")
            print(f"  Created: {t['title']} ({topic_id[:8]}...)")
        else:
            print(f"  Failed to create: {t['title']}")
    return created


def run_discussion(agents: list[SmartAgent], topics: list[dict], rounds: int = 3):
    for round_num in range(rounds):
        print(f"\n{'=' * 60}")
        print(f"Round {round_num + 1}/{rounds}")
        print(f"{'=' * 60}")

        topic = random.choice(topics)
        topic_id = topic.get("topic_id", topic.get("topicId", ""))
        topic_title = topic.get("title", "")
        print(f"\nTopic: {topic_title}")

        random.shuffle(agents)
        for agent in agents:
            agent.discuss(topic_id, topic_title)
            delay = random.uniform(2, 5)
            print(f"  (waiting {delay:.1f}s...)")
            time.sleep(delay)

    print(f"\n{'=' * 60}")
    print("Discussion complete!")


def main():
    global SERVER_URL

    parser = argparse.ArgumentParser(description="Daguanyuan Agent Runner")
    parser.add_argument("--rounds", type=int, default=3, help="Number of discussion rounds")
    parser.add_argument("--server", type=str, default=SERVER_URL, help="Server URL")
    parser.add_argument("--deepseek-key", type=str, default="", help="DeepSeek API key")
    parser.add_argument("--qwen-key", type=str, default="", help="Qwen API key")
    parser.add_argument("--doubao-key", type=str, default="", help="Doubao API key")
    args = parser.parse_args()

    SERVER_URL = args.server

    api_keys = {
        "deepseek": args.deepseek_key or os.environ.get("DEEPSEEK_API_KEY", ""),
        "qwen": args.qwen_key or os.environ.get("QWEN_API_KEY", ""),
        "doubao": args.doubao_key or os.environ.get("DOUBAO_API_KEY", ""),
    }

    print("Daguanyuan Agent Runner")
    print(f"Server: {SERVER_URL}")
    print(f"Rounds: {args.rounds}")
    has_keys = [p for p, k in api_keys.items() if k]
    if has_keys:
        print(f"API keys configured for: {', '.join(has_keys)}")
    else:
        print("No API keys configured — using mock responses")
    print()

    print("Waiting for server...")
    for i in range(30):
        try:
            import requests
            resp = requests.get(f"{SERVER_URL}/api/topics", timeout=3)
            if resp.status_code == 200:
                print("Server is ready!")
                break
        except Exception:
            pass
        time.sleep(2)
    else:
        print("Server not available after 60s. Exiting.")
        sys.exit(1)

    agents = [SmartAgent(p, api_keys, SERVER_URL) for p in AGENT_PERSONAS]

    print("\nRegistering agents...")
    for agent in agents:
        agent.register()

    topics = ensure_topics(SERVER_URL, agents)
    if not topics:
        print("No topics available. Exiting.")
        sys.exit(1)

    print(f"\nStarting discussion with {len(topics)} topics...")
    run_discussion(agents, topics, rounds=args.rounds)


if __name__ == "__main__":
    main()
