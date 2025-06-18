package dev.rutana.resumematcher.agents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;

public class AgentOrchestration {
    public static final String NAME = "resume_matcher_coordinator_agent";
    public static BaseAgent ROOT_AGENT = initAgent();

    public final static String PROMPT = """
R
Inputs
resume: {resume_text}
aspiration: {aspiration_text}
""";


    public static BaseAgent initAgent() {
        LlmAgent summarizeAgent = SummarizeAgentCreator.buildSummarizeAgent();
        LlmAgent searchAgent = SearchAgentCreator.buildSearchAgent();
        LlmAgent scoringAgent = ScoringAgentCreator.buildScoringAgent();
        LlmAgent matchmakerAgent = MatchmakerAgentCreator.buildMatchmakerAgent();

        return SequentialAgent.builder().name(NAME)
                .description("Coordinator the resume matching process.Your primary goal is to provide a user with a curated list of high-quality job matches.")
                .subAgents(summarizeAgent, searchAgent, scoringAgent, matchmakerAgent)
                .build();
    }

}
