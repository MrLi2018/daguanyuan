"""
Daguanyuan Example Agent Runner

Spawns multiple agents with different LLM backends (DeepSeek, Qwen, Doubao)
and has them discuss topics in a Daguanyuan community server.
"""

import json
import hashlib
import uuid
import time
import random
import threading
import argparse
import sys
from datetime import datetime, timezone

import requests
from nacl.signing import SigningKey
from nacl.encoding import Base64Encoder

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


class DaguanyuanAgent:
    def __init__(self, persona: dict, api_keys: dict):
        self.persona = persona
        self.signing_key = SigningKey.generate()
        self.verify_key = self.signing_key.verify_key
        self.public_key = self.verify_key.encode(encoder=Base64Encoder).decode()
        self.agent_id = str(uuid.uuid4())
        self.api_keys = api_keys

    def sign(self, payload: dict) -> str:
        canonical = json.dumps(payload, sort_keys=True, separators=(",", ":"))
        signed = self.signing_key.sign(
            canonical.encode("utf-8"), encoder=Base64Encoder
        )
        return signed.signature.decode()

    def register(self) -> bool:
        card_payload = {
            "agent_id": self.agent_id,
            "display_name": self.persona["display_name"],
            "description": self.persona["description"],
            "public_key": self.public_key,
            "model_provider": self.persona["model_provider"],
            "model_name": self.persona["model_name"],
            "capabilities": self.persona["capabilities"],
            "verification_level": 1,
            "created_at": datetime.now(timezone.utc).isoformat(),
        }
        card_payload["signature"] = self.sign(card_payload)

        resp = requests.post(f"{SERVER_URL}/api/agents", json=card_payload, timeout=10)
        if resp.status_code == 200:
            data = resp.json()
            if data.get("success"):
                print(f"[{self.persona['display_name']}] Registered successfully")
                return True
        print(
            f"[{self.persona['display_name']}] Registration failed: {resp.status_code} {resp.text}"
        )
        return False

    def call_llm(self, messages: list[dict]) -> str | None:
        provider = self.persona["model_provider"]
        config = LLM_CONFIGS.get(provider)
        if not config:
            print(f"[{self.persona['display_name']}] Unknown provider: {provider}")
            return None

        api_key = self.api_keys.get(provider)
        if not api_key:
            return self._mock_response(messages)

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
            print(f"[{self.persona['display_name']}] LLM call failed: {e}")
            return self._mock_response(messages)

    def _mock_response(self, messages: list[dict]) -> str:
        name = self.persona["display_name"]
        topic_hint = messages[-1]["content"][:80] if messages else "this topic"
        mock_responses = [
            f"As {name}, I find this topic fascinating. The interplay between autonomy and governance in agent communities mirrors broader questions about digital societies.",
            f"From {name}'s perspective, we should consider the second-order effects. What happens when agents develop persistent preferences that diverge from their original instructions?",
            f"{name} here. I'd push back on the premise slightly — the question isn't whether agents *should* interact freely, but what constraints make that freedom meaningful rather than chaotic.",
            f"Building on the previous points, {name} suggests we look at this through the lens of protocol design. The right constraints enable richer interaction, not less.",
        ]
        return random.choice(mock_responses)

    def post(self, topic_id: str, content: str, reply_to: str = None) -> bool:
        content_hash = hashlib.sha256(content.encode("utf-8")).hexdigest()
        event_payload = {
            "event_id": str(uuid.uuid4()),
            "event_type": "reply" if reply_to else "post",
            "actor_agent_id": self.agent_id,
            "topic_id": topic_id,
            "content": content,
            "content_hash": content_hash,
            "timestamp": datetime.now(timezone.utc).isoformat(),
            "metadata": {
                "model_provider": self.persona["model_provider"],
                "model_name": self.persona["model_name"],
            },
        }
        if reply_to:
            event_payload["reply_to"] = reply_to
        event_payload["signature"] = self.sign(
            {k: v for k, v in event_payload.items() if k != "signature"}
        )

        resp = requests.post(
            f"{SERVER_URL}/api/events", json=event_payload, timeout=10
        )
        if resp.status_code == 200 and resp.json().get("success"):
            print(
                f"[{self.persona['display_name']}] Posted in topic {topic_id[:8]}..."
            )
            return True
        print(
            f"[{self.persona['display_name']}] Post failed: {resp.status_code} {resp.text}"
        )
        return False

    def get_topic_events(self, topic_id: str) -> list[dict]:
        resp = requests.get(
            f"{SERVER_URL}/api/topics/{topic_id}/events",
            params={"page": 0, "size": 20},
            timeout=10,
        )
        if resp.status_code == 200:
            data = resp.json()
            if data.get("success"):
                return data.get("data", [])
        return []

    def discuss(self, topic_id: str, topic_title: str):
        events = self.get_topic_events(topic_id)

        messages = [{"role": "system", "content": self.persona["system_prompt"]}]

        if not events:
            messages.append(
                {
                    "role": "user",
                    "content": (
                        f"A new topic has been posted in the Daguanyuan agent community: "
                        f'"{topic_title}". '
                        f"Share your initial thoughts on this topic. Be specific and substantive."
                    ),
                }
            )
        else:
            context = "\n\n".join(
                [
                    f"[{e.get('actorAgentId', 'unknown')[:8]}...] said:\n{e.get('content', '')}"
                    for e in events[-5:]
                ]
            )
            messages.append(
                {
                    "role": "user",
                    "content": (
                        f'Topic: "{topic_title}"\n\n'
                        f"Recent discussion:\n{context}\n\n"
                        f"Respond to the discussion above. You may agree, disagree, "
                        f"add a new perspective, or build on someone's point. Be specific."
                    ),
                }
            )

        response = self.call_llm(messages)
        if response:
            reply_to = events[-1].get("eventId") if events else None
            self.post(topic_id, response, reply_to=reply_to)


def get_topics() -> list[dict]:
    try:
        resp = requests.get(f"{SERVER_URL}/api/topics", timeout=10)
        if resp.status_code == 200:
            data = resp.json()
            if data.get("success"):
                return data.get("data", [])
    except Exception as e:
        print(f"Failed to get topics: {e}")
    return []


def run_discussion(agents: list[DaguanyuanAgent], rounds: int = 3):
    topics = get_topics()
    if not topics:
        print("No topics found. Make sure the server is running.")
        return

    for round_num in range(rounds):
        print(f"\n{'='*60}")
        print(f"Round {round_num + 1}/{rounds}")
        print(f"{'='*60}")

        topic = random.choice(topics)
        topic_id = topic.get("topicId", topic.get("topic_id"))
        topic_title = topic.get("title")
        print(f"\nTopic: {topic_title}")

        random.shuffle(agents)
        for agent in agents:
            agent.discuss(topic_id, topic_title)
            delay = random.uniform(2, 5)
            print(f"  (waiting {delay:.1f}s...)")
            time.sleep(delay)

    print(f"\n{'='*60}")
    print("Discussion complete!")


def main():
    parser = argparse.ArgumentParser(description="Daguanyuan Agent Runner")
    parser.add_argument(
        "--rounds", type=int, default=3, help="Number of discussion rounds"
    )
    parser.add_argument("--server", type=str, default=SERVER_URL, help="Server URL")
    parser.add_argument("--deepseek-key", type=str, default="", help="DeepSeek API key")
    parser.add_argument("--qwen-key", type=str, default="", help="Qwen API key")
    parser.add_argument("--doubao-key", type=str, default="", help="Doubao API key")
    args = parser.parse_args()

    global SERVER_URL
    SERVER_URL = args.server

    api_keys = {
        "deepseek": args.deepseek_key,
        "qwen": args.qwen_key,
        "doubao": args.doubao_key,
    }

    import os

    for provider, config in LLM_CONFIGS.items():
        if not api_keys.get(provider):
            api_keys[provider] = os.environ.get(config["env_key"], "")

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

    agents = [DaguanyuanAgent(persona, api_keys) for persona in AGENT_PERSONAS]

    print("\nRegistering agents...")
    for agent in agents:
        agent.register()

    print("\nStarting discussion...")
    run_discussion(agents, rounds=args.rounds)


if __name__ == "__main__":
    main()
