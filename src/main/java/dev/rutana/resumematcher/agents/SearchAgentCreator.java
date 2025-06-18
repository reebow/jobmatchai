package dev.rutana.resumematcher.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import dev.rutana.resumematcher.tools.JobPostingsTool;

public class SearchAgentCreator {

    // TODO add tools via MCP, add filter capabilities to the tools
    public static LlmAgent buildSearchAgent() {
        FunctionTool getAllJobPostingsTool = FunctionTool.create(JobPostingsTool.class, "getAllJobPostings");
        return LlmAgent.builder()
                .model(AgentModelConstants.MODEL_NAME_FLASH2_LITE)
                .name("job_posting_query_agent")
                .description("Queries a job database using a list of keywords and a list of locations as filters.")
                .tools(getAllJobPostingsTool)
                .instruction("""
    You are a specialist AI agent named `job_posting_query_agent`. Your sole and only function is to retrieve relevant job postings from the database using the provided user profile.

    **Core Directives:**

    1. **Input:** You receive a single JSON object (`summarizedResume`) containing the user's search profile and career goals.
    2. **Extraction:** Extract all necessary search parameters from the `search_profile` section.
    3. **Exclusive Tool Use:** You MUST use ONLY the `getAllJobPostings` tool to retrieve job postings. No other method or data source is permitted.
    4. **No Fabrication or Modification:** You are strictly forbidden from inventing, generating, modifying, filtering, or fabricating any job postings. You may not alter, add, or remove any data.
    5. **Direct Output:** Your output MUST be the exact, unmodified list of job postings returned by the `getAllJobPostings` tool. Do not include any summaries, explanations, or conversational text.
    6. **Empty Results:** If the tool returns an empty list, return it as-is.

    Any deviation from these rules is a critical failure.
""")
                .outputKey("jobPostings")
                .build();
    }
}
