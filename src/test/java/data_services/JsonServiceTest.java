package data_services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonServiceTest {

    private final JsonService jsonService = new JsonService();

    @Test
    void convertsListToJsonArray() {
        String json = jsonService.toJson(List.of(1, 2, 3));

        assertEquals("[1,2,3]", json);
    }

    @Test
    void convertsMapToJsonObject() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("x", 1);
        data.put("y", -2);
        data.put("hit", true);

        String json = jsonService.toJson(data);

        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> parsed = mapper.readValue(json, Map.class);

        assertEquals(1, parsed.get("x"));
        assertEquals(-2, parsed.get("y"));
        assertEquals(true, parsed.get("hit"));
    }
}