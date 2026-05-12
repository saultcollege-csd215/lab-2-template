package sales.feature.base.validation;

import java.util.HashMap;
import java.util.Map;

public class ValidationMessages {

    private final Map<String, String> messages = new HashMap<>();

    public void add(String key, String message) {
        messages.put(key, message);
    }

    public boolean has(String key) {
        return messages.containsKey(key);
    }

    public String get(String key) {
        return has(key) ? messages.get(key) : "";
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public static ValidationMessages none() {
        return new ValidationMessages();
    }
}
