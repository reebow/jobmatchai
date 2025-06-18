package dev.rutana.resumematcher.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

public final class FileTextExtractorUtil {

    private FileTextExtractorUtil() {
        // Private constructor to prevent instantiation
    }

    public static String extractText(MultipartFile resume) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();

        try (InputStream resumeInputStream = resume.getInputStream()) {
            parser.parse(resumeInputStream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Error extracting text from file", e);
        }
        return handler.toString();
    }
}
