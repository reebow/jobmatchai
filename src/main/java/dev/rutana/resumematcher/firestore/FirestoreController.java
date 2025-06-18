package dev.rutana.resumematcher.firestore;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.rutana.resumematcher.models.JobPosting;

@RestController
@RequestMapping("/api/jobpostings")
public class FirestoreController {

    private final FirestoreService firestoreService;

    public FirestoreController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    @GetMapping
    public List<JobPosting> getAllJobPostings() {
        return firestoreService.getAllJobPostings();
    }
}
