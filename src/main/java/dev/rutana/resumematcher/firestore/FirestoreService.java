package dev.rutana.resumematcher.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import dev.rutana.resumematcher.models.JobPosting;
import dev.rutana.resumematcher.exceptions.ResumeMatcherException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
public class FirestoreService {

    private final CollectionReference jobPostingsCollection;

   
    public FirestoreService(Firestore firestore) {
        this.jobPostingsCollection = firestore.collection("jobPostings");
    }

    /**
     * Retrieves all job postings from the Firestore database.
     *
     * @return A list of JobPosting objects representing all job postings.
     * @throws ResumeMatcherException if there is an error retrieving the job postings.
     */
    public List<JobPosting> getAllJobPostings() {
        try {
            ApiFuture<QuerySnapshot> future = jobPostingsCollection.get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            return documents.stream()
                    .map(doc -> doc.toObject(JobPosting.class))
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new ResumeMatcherException("Thread was interrupted while waiting for Firestore response", e);
        } catch (ExecutionException e) {
            throw new ResumeMatcherException("Error executing Firestore query", e);
        }
    }
}
