# LLM Software Testing Assistant

An LLM-enabled Spring Boot chatbot application with AI-assisted test generation, MCP tooling, feedback collection, and evaluation strategies for testing LLM behavior.

## Overview

This project explores how Large Language Models can be integrated into software systems and how agentic testing workflows can improve application quality. The system includes a Spring Boot chatbot with multiple interaction modes, feedback submission, input validation, and summarization features.

## Key Features

- **LLM Chatbot API**: Accepts user messages and returns LLM-generated responses.
- **Input Validation**: Rejects empty or overly long prompts before they reach the model.
- **Chat Modes**: Supports default, Java tutor, debugging helper, and scheduling assistant modes.
- **Feedback Collection**: Allows users to rate chatbot responses and submit comments.
- **Summarization Endpoint**: Generates concise summaries from user-provided text.
- **AI-Assisted Testing Workflow**: Uses MCP tooling to support test generation, execution, and iterative improvement.

## Tech Stack

- Java
- Spring Boot
- Maven
- JUnit
- JaCoCo
- Python
- FastMCP / Model Context Protocol
- GitHub MCP tools
- VS Code

## Repository Structure

\`\`\`text
MCPServerProject/      # FastMCP server and MCP tool setup
llm-enabled-se/        # Initial LLM-enabled chatbot application
sprint2-app/           # Extended chatbot app with testing and new features
se333-demo/            # Demo project for agentic test generation
img/                   # Project screenshots and documentation assets
\`\`\`

## Testing Approach

This project combines traditional automated testing with LLM-specific evaluation.

Traditional tests verify:

- API endpoint behavior
- Request validation
- Response structure
- Feedback submission
- Chat mode handling
- Application stability

LLM behavior was evaluated using qualitative criteria because LLM outputs can vary between runs. Responses were reviewed for:

- Relevance
- Helpfulness
- Safety
- Clarity
- Consistency with selected chat mode

## MCP Testing Agent

The MCP testing workflow was designed to:

1. Generate test cases.
2. Run Maven tests.
3. Parse test and coverage results.
4. Identify missing coverage.
5. Improve tests iteratively.
6. Track changes through Git commits.

## Setup

### Prerequisites

- Java 21+
- Maven
- Python 3.10+
- Node.js 18+
- VS Code

### Run the Spring Boot App

\`\`\`bash
cd sprint2-app
mvn spring-boot:run
\`\`\`

### Run Tests

\`\`\`bash
cd sprint2-app
mvn test
\`\`\`

### Run MCP Server

\`\`\`bash
cd MCPServerProject
python server.py
\`\`\`

## What I Learned

- Built and extended an LLM-enabled application.
- Tested nondeterministic AI behavior using practical evaluation criteria.
- Used MCP tools for agentic software testing workflows.
- Combined automated tests with qualitative LLM evaluation.
- Managed feature development through Git branches and commits.

## Contributors

- Kaamil Khan
- Allen Espinosa
- Tammy Sanchez
