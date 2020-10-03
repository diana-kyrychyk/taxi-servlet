package ua.com.taxi.util;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ValidationMessageLocalizator {

    private static final String VALIDATION_BUNDLE_NAME = "ValidationMessages";

    public void localize(Map<String, String> fieldErrors, Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle(VALIDATION_BUNDLE_NAME, locale);
        for (Map.Entry<String, String> entry : fieldErrors.entrySet()) {
            if (isTemplate(entry.getValue())) {
                String key = extractKey(entry.getValue());
                String message = bundle.getString(key);
                entry.setValue(message);
            }
        }
    }

    private boolean isTemplate(String message) {
        return message.startsWith("{");
    }

    private String extractKey(String template) {
        return template.substring(1, template.length() - 1);
    }

}
