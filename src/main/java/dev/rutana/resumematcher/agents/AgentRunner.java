package dev.rutana.resumematcher.agents;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.stereotype.Service;

import static dev.rutana.resumematcher.agents.AgentOrchestration.ROOT_AGENT;

@Service
public class AgentRunner {

    private static String USER_ID = "student";

    public String startAgent(String resumeText, String asperation) {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);
        Session session = runner.sessionService().createSession(AgentOrchestration.NAME, USER_ID).blockingGet();

        String queryAndContent = AgentOrchestration.PROMPT.replace("resume_text", resumeText).replace("aspiration_text", asperation);
        Content userMsg = Content.fromParts(Part.fromText(queryAndContent));

        Flowable<Event> events = runner.runAsync(USER_ID, session.id(), userMsg);

        String result = events.blockingLast().stringifyContent();

        result = result.replace("```markdown", "")
                .replace("```", "");


        return result;
    }
}
