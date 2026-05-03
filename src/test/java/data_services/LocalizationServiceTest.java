package data_services;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizationServiceTest {

    @Test
    void shouldReadRussianMessageFromLocalizationFile() {
        LocalizationService service = new LocalizationService(Locale.forLanguageTag("ru"));

        assertEquals("Ошибка ввода", service.get("error.invalid.input"));
    }

    @Test
    void shouldReadDefaultMessageFromLocalizationFile() {
        LocalizationService service = new LocalizationService(Locale.ENGLISH);

        assertEquals("Invalid input", service.get("error.invalid.input"));
    }
}