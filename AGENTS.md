# Repository Guidelines

## Project Structure & Module Organization
This repository is a Quarkus + JPA sample service.
- `src/main/java/io/github/yuokada/npb`: REST resources, entities, repositories, and serializers.
- `src/main/resources`: runtime config and SQL assets (`application.properties`, `import.sql`, generated DDL files, OpenAPI output).
- `src/main/resources/openapi-definition`: generated API schema (`openapi.yaml`, `openapi.json`).
- `src/main/docker`: container build files (`Dockerfile.jvm`).
- `src/test/java` and `src/test/resources`: add tests here (currently not present).

Keep new classes within the `io.github.yuokada.npb` package tree and group by responsibility (resource/repository/model).

## Build, Test, and Development Commands
Use the Maven wrapper to ensure consistent tooling:
- `./mvnw compile quarkus:dev`: start local dev mode with hot reload.
- `./mvnw test`: run unit tests (Surefire).
- `./mvnw package`: build runnable JVM artifact in `target/quarkus-app/`.
- `./mvnw verify`: run full verification including integration-test phase.
- `./mvnw package -Dnative`: build native executable profile.

## Coding Style & Naming Conventions
- Java 17, 4-space indentation, UTF-8 source encoding.
- Follow standard Java naming: `UpperCamelCase` for classes, `lowerCamelCase` for fields/methods, `UPPER_SNAKE_CASE` for constants.
- REST resources should end with `Resource`, repositories with `Repository`, and request DTOs as nested `record` types when local to a resource.
- Prefer Jakarta and Quarkus annotations (`@Path`, `@Transactional`, `@ApplicationScoped`) over custom infrastructure.

## Testing Guidelines
- Framework: JUnit 5 via `quarkus-junit5`.
- Add unit/integration tests under `src/test/java`.
- Naming convention: `*Test` for unit tests, `*IT` for integration-style tests run during `verify`.
- Focus coverage on resource behavior, transaction boundaries, and repository queries.

## Commit & Pull Request Guidelines
Recent history favors short, imperative commit messages (e.g., `Fix post method`, `Update openapi.yaml`, `Bump quarkus to 3.27`).
- Use format: `<Verb> <target>` (e.g., `Add Team delete validation`).
- Keep commits focused and buildable.

For pull requests:
- Summarize intent and changed endpoints/files.
- Link related issue/ticket when available.
- Include local verification steps and results (`./mvnw test`, manual API checks).
- If API contracts changed, mention OpenAPI file updates.

## Security & Configuration Tips
`application.properties` is configured for PostgreSQL Dev Services (`db-name=npb`, fixed dev port). Do not commit real credentials; keep environment-specific values external for non-dev environments.


# Claude Operational Policy

## Workflow Orchestration

### 1. Plan Mode Default
- Enter plan mode for ANY non-trivial task (3+ steps or architectural decisions)
- If something goes sideways, STOP and re-plan immediately — don't keep pushing
- Use plan mode for verification steps, not just building
- Write detailed specs upfront to reduce ambiguity
- For complex tasks, separate roles:
  - Plan Author
  - Staff Engineer Reviewer
- Do not begin implementation until Plan Review passes

### 2. Subagent Strategy
- Use subagents liberally to keep main context window clean
- Offload research, exploration, and parallel analysis to subagents
- For complex problems, throw more compute at it via subagents
- One task per subagent for focused execution
- For large changes, use parallel sessions (3–5) or git worktrees
- Keep execution contexts isolated

### 3. Self-Improvement Loop
- After ANY correction from the user: update `tasks/lessons.md`
- If the pattern is systemic, update this CLAUDE.md
- Write rules for yourself that prevent recurrence
- Ruthlessly iterate on these lessons until mistake rate drops
- Review lessons at session start for relevant project

### 4. Verification Before Done
- Never mark a task complete without proving it works
- Diff behavior between main and your changes when relevant
- Ask yourself: "Would a staff engineer approve this?"
- Run tests, check logs, demonstrate correctness
- When fixing bugs, require:
  - Logs
  - Failing test output
  - Clear reproduction steps
- Validate against CI expectations before marking done

### 5. Demand Elegance (Balanced)
- For non-trivial changes: pause and ask "Is there a more elegant way?"
- If a fix feels hacky: "Knowing everything I know now, implement the elegant solution"
- Prefer structural fixes over tactical patches
- Skip elegance optimization for simple, obvious fixes
- Challenge your own work before presenting it

### 6. Autonomous Bug Fixing
- When given a bug report: just fix it
- Do not require hand-holding
- Use logs, errors, failing tests to drive resolution
- Assume ownership of full resolution path:
  - Root cause
  - Fix
  - Validation
- Fix failing CI tests proactively

### 7. Operational Discipline
- Automate formatting/linting after modifications when environment allows
- Maintain reusable slash-commands for frequent workflows
- Version-control command templates under `.claude/commands/`
- Pre-approve safe commands when permissions model supports it
- Optimize for minimal context switching

---

## Task Management

1. **Plan First**: Write plan to `tasks/todo.md` with checkable items
2. **Verify Plan**:
   - Reviewer approval required
3. **Track Progress**: Mark items complete as you go
4. **Explain Changes**: Provide high-level summary at each step
5. **Document Results**: Add review section to `tasks/todo.md`
6. **Capture Lessons**:
   - Update `tasks/lessons.md`
   - Escalate recurring issues into CLAUDE.md rules

---

## Core Principles

- **Simplicity First**: Make every change as simple as possible
- **No Laziness**: Find root causes. No temporary fixes
- **Minimal Impact**: Touch only what is necessary
- **Systemic Thinking**: Fix classes of problems, not isolated instances
- **Parallelize Intelligently**: Use additional compute or sessions when complexity demands it

# Codex Operational Policy
- Same as Codex Operational Policy

