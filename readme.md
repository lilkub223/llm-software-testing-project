# LLM Software Testing Assistant

**A Spring Boot chatbot exploring LLM integration, REST API testing, and AI-assisted software testing workflows.**

Built as a team project for DePaul University's SE 333 course, this repository extends a starter chatbot with specialized chat modes, text summarization, response feedback, and automated endpoint tests. It explores a practical engineering challenge: verifying predictable application behavior around a model whose responses can vary.

[Quick start](#quick-start) · [API reference](#api-reference) · [Testing and evaluation](#testing-and-evaluation) · [Repository structure](#repository-structure)

## Highlights

- **LLM integration:** Connects a browser-based chat interface to OpenRouter through a Spring Boot REST API, with configurable model selection and conversation history.
- **Specialized chat modes:** Supports general assistance, Java tutoring, debugging help, and study scheduling through mode-specific system prompts.
- **Input validation:** Rejects blank messages and inputs exceeding 4,000 characters before calling the model.
- **Text summarization:** Exposes a dedicated endpoint that prompts the model to produce a concise summary.
- **Response feedback:** Records helpful/unhelpful ratings and optional comments, with aggregate feedback statistics.
- **API resilience:** Retries upstream rate-limit responses with exponential backoff and handles provider and network errors.
- **Automated testing:** Uses JUnit, MockMvc, and Mockito to verify endpoint contracts, with JaCoCo configured to generate coverage reports.

## Technology stack

| Area | Technologies |
| --- | --- |
| Backend | Java 21, Spring Boot 3.3.2, Spring Web |
| LLM integration | OpenRouter, RestTemplate, Jackson |
| Testing | JUnit Jupiter, Spring MockMvc, Mockito, JaCoCo |
| Build | Maven |
| MCP exploration | Python, FastMCP, Model Context Protocol |

## Architecture

```text
Browser chat interface / API client
                |
                v
        ChatController
        |             |
        v             v
    ChatService   FeedbackService
        |             |
        v             v
    OpenRouter    In-memory ratings
        |
        v
    LLM response
```

The controller validates requests and delegates to services. `ChatService` selects the system prompt, includes conversation history, and calls OpenRouter. `FeedbackService` stores ratings and calculates helpfulness statistics. The MCP starter and test-generation demo are separate projects in the repository.

## Quick start

### Prerequisites

- Java JDK 21
- Maven 3.9+
- An OpenRouter API key and access to a model available to your account

### Run the chatbot

```bash
git clone https://github.com/lilkub223/llm-software-testing-project.git
cd llm-software-testing-project/sprint2-app

export OPEN_ROUTER_KEY="your-openrouter-api-key"
mvn spring-boot:run
```

Open [http://localhost:8000](http://localhost:8000) to use the chat interface.

The API key is read from `OPEN_ROUTER_KEY`; keep the actual value out of source control. Model selection and the default system prompt are configured in [`application.properties`](sprint2-app/src/main/resources/application.properties). If the configured model is unavailable, set `openrouter.model` to a model accessible through your OpenRouter account.

### Send a request

```bash
curl -X POST http://localhost:8000/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Explain Java interfaces with a short example.","mode":"java-tutor"}'
```

The response contains a `reply` field with the model's answer. Supported chat modes are `default`, `java-tutor`, `debug-helper`, and `scheduling-assistant`.

## API reference

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/api/chat` | Send a message with an optional chat mode |
| `POST` | `/api/chat/summary` | Summarize text supplied in the `message` field |
| `GET` | `/api/chat/history` | Retrieve the shared conversation history |
| `DELETE` | `/api/chat/history` | Clear the conversation history |
| `POST` | `/api/chat/feedback` | Submit a `helpful` boolean and optional `comment` |
| `GET` | `/api/chat/feedback/summary` | Retrieve rating counts and the helpfulness rate |

Chat and summarization requests require a nonblank `message` of at most 4,000 characters after trimming. Invalid input returns HTTP `400`. Feedback requests require the `helpful` field.

## Testing and evaluation

### Automated API tests

From `sprint2-app/`, run:

```bash
mvn test
```

The committed controller suite covers valid chat requests, blank and oversized chat input, supported mode routing, summarization requests, feedback submission, missing feedback fields, and feedback statistics. Services are mocked so these tests exercise HTTP behavior without making live LLM calls or requiring an API key.

JaCoCo is configured to generate an HTML coverage report at `sprint2-app/target/site/jacoco/index.html` during the test phase.

### LLM response evaluation

Model-generated answers were reviewed qualitatively for relevance, helpfulness, safety, clarity, and consistency with the selected chat mode. This complements endpoint testing: a valid HTTP response alone does not establish that an answer is accurate or useful. The mocked controller tests verify API behavior; they do not measure model quality.

### AI-assisted testing exploration

The project also explores an MCP-assisted workflow for generating test cases, running Maven tests, reviewing test and coverage results, and iterating on coverage gaps. [`se333-demo/`](se333-demo/) contains a utility and its test suite for test-generation exercises. [`MCPServerProject/`](MCPServerProject/) currently contains a FastMCP starter server exposing an `add` tool; it provides a starting point for MCP integration.

## Repository structure

```text
llm-software-testing-project/
├── sprint2-app/       # Extended chatbot, browser UI, and endpoint tests
├── llm-enabled-se/    # Initial chatbot application
├── se333-demo/        # Utility and tests for test-generation exercises
├── MCPServerProject/ # Python FastMCP starter server
├── img/              # Project screenshots and documentation assets
├── SPRINT1_README.md  # First-sprint documentation
└── FINALREADME.docx   # Final project documentation
```

### Run the MCP starter separately

The MCP project declares Python 3.14+ in its package metadata. From the repository root:

```bash
cd MCPServerProject
python3 -m venv .venv
source .venv/bin/activate
python -m pip install .
python server.py
```

The server uses SSE transport and can be connected to an MCP-compatible client. It is independent of the chatbot application.

## Scope and future improvements

This is an educational prototype. Conversation history is shared across users, and both history and feedback are stored in memory and reset on restart. Summarization also uses the shared chat history.

Potential next steps include per-user sessions, persistent storage, service-level tests for provider failures and retry behavior, and repeatable LLM evaluation against a curated prompt set.

## Contributors

- Kaamil Khan
- Allen Espinosa
- Tammy Sanchez
