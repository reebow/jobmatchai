package dev.rutana.resumematcher.controllers;

import java.util.HashMap;
import java.util.Map;

import dev.rutana.resumematcher.agents.AgentRunner;
import dev.rutana.resumematcher.utils.FileTextExtractorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ResumeMatcherController {

    private final AgentRunner agentRunner;

    public ResumeMatcherController(AgentRunner agentRunner) {
        this.agentRunner = agentRunner;
    }

    @GetMapping("/")
    public String showResumeMatcherPage() {
        return "resumeMatcher";
    }

    // todo currently there is no user input validation, the user try to manipulate the agent response
    @PostMapping("/resume-matcher")
    @ResponseBody
    public Map<String, Object> handleResumeUpload(@RequestParam("resume") MultipartFile resume, @RequestParam(value = "aspirations", required = false) String aspirations) {
        Map<String, Object> response = new HashMap<>();

        if (resume.isEmpty()) {
            response.put("error", "Please upload a resume.");
            return response;
        }
        if (!StringUtils.hasText(aspirations)) {
            aspirations = "No aspirations provided, use only the resume";
        }

        String fileContent = FileTextExtractorUtil.extractText(resume);
        String result = agentRunner.startAgent(fileContent, aspirations);

        response.put("result", result);

        return response;
    }
}
