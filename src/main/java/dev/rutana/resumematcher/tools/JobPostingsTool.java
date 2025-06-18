package dev.rutana.resumematcher.tools;

import dev.rutana.resumematcher.exceptions.ResumeMatcherException;
import dev.rutana.resumematcher.firestore.FirestoreService;
import dev.rutana.resumematcher.models.JobPosting;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * A wrapper class to provide static access to the Spring-managed
 * FirestoreService.
 * Needed as tool methods in AI agents must be static.
 */
@Component
public class JobPostingsTool {

    private static final Logger logger = LoggerFactory.getLogger(JobPostingsTool.class);

    private static FirestoreService firestoreService;

    private final FirestoreService injectedFirestoreService;

    public JobPostingsTool(FirestoreService firestoreService) {
        this.injectedFirestoreService = firestoreService;
    }


    @PostConstruct
    private void init() {
        JobPostingsTool.firestoreService = this.injectedFirestoreService;
    }

    /**
     *
     * @return A Map containing a status and a report of all job postings.
     */
    public static Map<String, String> getAllJobPostings() {
        try {
            if (firestoreService == null) {
                return fetchJobPostingsFromWebApi();
            }
            return fetchJobPostingsFromFirestore();
        } catch (ResumeMatcherException e) {
            return handleError(e);
        }
    }

    /**
     *
     * This is called when running the adk provided webserver as the beans are not initialized yet.
     * TODO find a way to tell spring to initialize the beans of my project.
     * Fetches job postings from the web API if FirestoreService is not initialized.

     * @return A Map containing the status and report of job postings.
     */
    private static Map<String, String> fetchJobPostingsFromWebApi() {
        System.out.println("FirestoreService is not initialized. Using the web API as a fallback.");

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8081/api/jobpostings";

        try {
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            if (jsonResponse == null || jsonResponse.isBlank()) {
                return Map.of(
                        "status", "success",
                        "report", "No job postings were found via the web API.");
            }

            return Map.of("status", "success", "report", jsonResponse);

        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "report", "An error occurred while calling the web API: " + e.getMessage());
        }
    }

    private static Map<String, String> fetchJobPostingsFromFirestore() {
        List<JobPosting> postings = firestoreService.getAllJobPostings();

        if (postings.isEmpty()) {
            return Map.of(
                    "status", "success",
                    "report", "No job postings were found in the database.");
        }

        return Map.of("status", "success", "report", postings.toString());
    }


    private static Map<String, String> handleError(ResumeMatcherException e) {
        logger.error("An error occurred while fetching job postings", e);
        return Map.of(
                "status", "error",
                "report", "An error occurred while fetching job postings: " + e.getMessage());
    }
}
