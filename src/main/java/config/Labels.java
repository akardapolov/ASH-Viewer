package config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

@Slf4j
public final class Labels {
    public static final String[] LANGUAGES = { "system", "en", "ru"};
    private static Labels instance;

    PropertyResourceBundle labels, labelsFallback;
    Locale locale;

    static {
        // this is needed for Visual Editor to display
        // labels at design time
        initialize(Locale.getDefault());
    }

    Labels() {}

    public static Labels getInstance() {
        return instance;
    }

    /**
     * Initializes the internal locale-specific data.
     * The files messages_lang.properties and messages.properties are searched for from the classpath.
     * This method must be called prior to using this class.
     */
    public static void initialize(Locale locale) {
        if (instance != null && locale.equals(instance.locale)) {
            // do not reload locale, because it was already initialized in the static block
            return;
        }

        // create a new instance
        instance = new Labels();

        instance.locale = locale;

        try (InputStream labelsStream = Labels.class.getClassLoader().getResourceAsStream("messages.properties")) {
            if (labelsStream == null) {
                throw new MissingResourceException("Labels not found!", Labels.class.getName(), "messages");
            }
            instance.labelsFallback = new PropertyResourceBundle(new InputStreamReader(labelsStream, "UTF-8"));
        }
        catch (IOException e) {
            throw new MissingResourceException(e.toString(), Labels.class.getName(), "messages");
        }

        try (InputStream labelsStream = Labels.class.getClassLoader().getResourceAsStream("messages_" + locale.toString() + ".properties")) {
            instance.labels = new PropertyResourceBundle(new InputStreamReader(labelsStream, "UTF-8"));
        }
        catch (Exception e) {
            try (InputStream labelsStream = Labels.class.getClassLoader().getResourceAsStream("messages_" + locale.getLanguage() + ".properties")) {
                instance.labels = new PropertyResourceBundle(new InputStreamReader(labelsStream, "UTF-8"));
            }
            catch (Exception e2) {
                instance.labels = instance.labelsFallback;
            }
        }
    }

    /**
     * Retrieves a String specified by the label key
     * @param key
     */
    public String get(String key) {
        try {
            return labels.getString(key);
        }
        catch (MissingResourceException e) {
            String text = labelsFallback.getString(key);
            log.warn("Used fallback label for " + key);
            return text;
        }
    }

    /**
     * A shortened form of Labels.getInstance().get()
     */
    public static String getLabel(String key) {
        return getInstance().get(key);
    }
}
