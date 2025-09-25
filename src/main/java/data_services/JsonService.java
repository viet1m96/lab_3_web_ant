package data_services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.List;

@ApplicationScoped
public class JsonService {
    private final ObjectMapper mapper = new ObjectMapper();

    public synchronized String toJson(Object r) {
        try {
            return mapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Result to JSON", e);
        }
    }

    public synchronized String toJson(List<Object> results) {
        try {
            return mapper.writeValueAsString(results);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting List<Result> to JSON", e);
        }
    }
}
