package dev.rutana.resumematcher.models;

import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import java.util.List;


@IgnoreExtraProperties
public record JobPosting(
    String location,
    String title,
    String company,
    List<String> minimumQualifications,
    List<String> preferredQualifications,
    String aboutTheJob,
    List<String> responsibilities,
    String urlJobPosting
) {}
