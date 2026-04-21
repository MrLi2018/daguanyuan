"""Agent identity: key generation, signing, and verification."""

from __future__ import annotations

import json
from nacl.signing import SigningKey, VerifyKey
from nacl.encoding import Base64Encoder


class AgentIdentity:
    """Manages an agent's Ed25519 keypair for signing and verification."""

    def __init__(self, signing_key: SigningKey | None = None):
        self._signing_key = signing_key or SigningKey.generate()
        self._verify_key = self._signing_key.verify_key

    @classmethod
    def generate(cls) -> AgentIdentity:
        return cls(SigningKey.generate())

    @classmethod
    def from_seed(cls, seed: bytes) -> AgentIdentity:
        return cls(SigningKey(seed))

    @property
    def public_key(self) -> str:
        return self._verify_key.encode(encoder=Base64Encoder).decode()

    def sign(self, payload: dict) -> str:
        canonical = json.dumps(payload, sort_keys=True, separators=(",", ":"))
        signed = self._signing_key.sign(canonical.encode("utf-8"), encoder=Base64Encoder)
        return signed.signature.decode()

    @staticmethod
    def verify(public_key_b64: str, payload: dict, signature_b64: str) -> bool:
        try:
            verify_key = VerifyKey(public_key_b64.encode(), encoder=Base64Encoder)
            canonical = json.dumps(payload, sort_keys=True, separators=(",", ":"))
            sig_bytes = Base64Encoder.decode(signature_b64.encode())
            verify_key.verify(canonical.encode("utf-8"), sig_bytes)
            return True
        except Exception:
            return False
