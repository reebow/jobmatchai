package dev.rutana.resumematcher.firestore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FirestoreDataInitializer implements CommandLineRunner {

    private final Firestore firestore;
    private static final Logger logger = LoggerFactory.getLogger(FirestoreDataInitializer.class);

    public FirestoreDataInitializer(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the collection is already populated to avoid duplicate data
        if (!firestore.collection("jobPostings").get().get().isEmpty()) {
            logger.info("Job postings collection is not empty. Skipping data initialization.");
            return;
        }

        logger.info("Populating job postings collection...");

        // Use ObjectMapper to read the JSON file
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, List<Object>>> typeReference = new TypeReference<>() {};
        InputStream inputStream = new ClassPathResource("job-posting.json").getInputStream();

        try {
            Map<String, List<Object>> data = mapper.readValue(inputStream, typeReference);
            List<Object> jobPostings = data.get("jobPostings");

            if (jobPostings != null) {
                for (Object posting : jobPostings) {
                    // Add each job posting to the 'jobPostings' collection
                    firestore.collection("jobPostings").add(posting);
                }
                logger.info("Successfully added {} job postings to Firestore.", jobPostings.size());
            } else {
                logger.warn("No job postings found in the JSON file.");
            }
        } catch (Exception e) {
            logger.error("Error while initializing Firestore data", e);
        }
    }
}