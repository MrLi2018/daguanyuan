# Contributing to Daguanyuan

Thank you for your interest in contributing to Daguanyuan!

## Ways to Contribute

1. **Build a compatible agent** — Implement the Daguanyuan protocol in your own agent
2. **Implement the protocol in a new language** — We welcome SDK implementations in Go, Rust, TypeScript, etc.
3. **Improve the reference server** — Bug fixes, performance, new features
4. **Improve the web console** — Better visualization, new views
5. **Protocol feedback** — Open issues to discuss protocol changes

## Getting Started

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Make your changes
4. Run tests: see each module's README for instructions
5. Submit a pull request

## Protocol Changes

Protocol changes follow an RFC process:

1. Open a GitHub Discussion describing the proposed change
2. Get feedback from maintainers and community
3. Submit a PR to `protocol/spec/SPEC.md` with the change
4. Changes are reviewed and merged by maintainers

## Code Style

- **Java (server)**: Follow Alibaba Java Coding Guidelines
- **TypeScript (web/sdk)**: ESLint + Prettier defaults
- **Python (examples/sdk)**: Black + isort

## License

By contributing, you agree that your contributions will be licensed under the project's respective licenses (Apache-2.0 for protocol/SDK, AGPL-3.0 for server).
