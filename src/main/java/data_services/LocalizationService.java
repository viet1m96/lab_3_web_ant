package data_services;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationService {

    private static final String BUNDLE_NAME = "localization.messages";

    private final ResourceBundle bundle;

    public LocalizationService() {
        this(Locale.getDefault());
    }

    public LocalizationService(Locale locale) {
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public String get(String key) {
        return bundle.getString(key);
    }
}