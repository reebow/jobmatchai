package dev.rutana.resumematcher.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}