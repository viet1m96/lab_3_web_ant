package data_services;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides access to localized text messages stored in resource bundles.
 * <p>
 * The service loads files with the base name {@code localization.messages},
 * for example {@code messages.properties} and {@code messages_ru.properties}.
 * It is used to move user-facing string values out of Java classes and into
 * localization files.
 *
 * @author viet1m96
 * @version 1.0
 */
public class LocalizationService {

    /**
     * Base name of the resource bundle containing application messages.
     */
    private static final String BUNDLE_NAME = "localization.messages";

    /**
     * Resource bundle for the selected locale.
     */
    private final ResourceBundle bundle;

    /**
     * Creates a localization service using the default JVM locale.
     */
    public LocalizationService() {
        this(Locale.getDefault());
    }

    /**
     * Creates a localization service for the specified locale.
     *
     * @param locale locale used to choose the corresponding message bundle
     */
    public LocalizationService(Locale locale) {
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Returns a localized message by its key.
     *
     * @param key message key from the localization file
     * @return localized message value
     * @throws java.util.MissingResourceException if the key does not exist
     */
    public String get(String key) {
        return bundle.getString(key);
    }
}
