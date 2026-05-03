package data_services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Application-scoped service for converting application objects to JSON.
 * <p>
 * The service wraps a shared Jackson {@link ObjectMapper} instance and provides
 * synchronized helper methods for serializing single objects and collections.
 * It is used when the server has to send point data or other application data
 * to the client-side JavaScript code.
 *
 * @author viet1m96
 * @version 1.0
 */
@ApplicationScoped
public class JsonService {
    /**
     * Jackson mapper used for all JSON serialization operations.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a single object to its JSON string representation.
     *
     * @param r object to serialize
     * @return JSON representation of the provided object
     * @throws RuntimeException if Jackson cannot serialize the object
     */
    public synchronized String toJson(Object r) {
        try {
            return mapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Result to JSON", e);
        }
    }

    /**
     * Converts a list of objects to a JSON array string.
     *
     * @param results list of objects to serialize
     * @return JSON array containing all objects from the provided list
     * @throws RuntimeException if Jackson cannot serialize the list
     */
    public synchronized String toJson(List<Object> results) {
        try {
            return mapper.writeValueAsString(results);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting List<Result> to JSON", e);
        }
    }
}
