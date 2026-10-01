# Sprint 1 README

Sprint 1 focused on creating an AI-assisted testing workflow. We set up the MCP server, created a tester agent prompt, and used the agent to generate and improve tests for a Java Maven project.

All Sprint 1 work was completed on the `sprint1` branch.

The MCP server was added in the `MCPServerProject/` folder and connected through VS Code. The tester prompt was created at `se333-demo/.github/prompts/tester.prompt.md`. This prompt defines how the agent should generate tests, run tests, check coverage, improve tests based on coverage gaps, and repeat the process when needed.

For testing, we used a Maven demo project located in `se333-demo/`. The main file tested was `LeftPadUtils.java`. The AI testing agent generated JUnit 5 tests for this file and covered normal cases, edge cases, and branch behavior.

Tests were run with: `cd se333-demo` and then `mvn test`.

Coverage was checked with: `mvn test jacoco:report`.

The JaCoCo report showed that `LeftPadUtils` reached 100% coverage after one testing iteration. The coverage report can be found at `se333-demo/target/site/jacoco/index.html`.

Phase 3 focused on operationalizing Git for AI-assisted development. We updated the tester agent prompt to include a GitHub MCP workflow so AI-generated or AI-modified changes are tracked through branches, commits, pushes, and pull requests. The workflow follows a trunk-based model where `main` is treated as the trunk branch, direct commits to `main` are avoided, and the agent works on short-lived feature branches.

Our automation strategy is to have the agent create pull requests after a meaningful set of related changes instead of after every small edit. We chose not to automatically merge AI-generated changes because human review is important for checking correctness, test quality, and maintainability. Additional quality gates include running tests, checking coverage, and reviewing changes before merging.

Overall, Sprint 1 established a working AI-assisted testing workflow using MCP, Maven, JUnit 5, JaCoCo, Git, and GitHub MCP tools. This workflow can now be applied to future sprints to test larger Java applications.
