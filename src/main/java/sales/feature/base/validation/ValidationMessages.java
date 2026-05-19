package sales.feature.base.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * A map-like class that holds a set of validation messages.
 * Each message may be set/accessed by way of a String key.
 */
public class ValidationMessages {

    /**
     * The set of stored messages
     */
    private final Map<String, String> messages = new HashMap<>();

    /**
     * Adds a message with the given key
     * @param key The key for the message
     * @param message The message
     */
    public void add(String key, String message) {
        messages.put(key, message);
    }

    /**
     * @param key The key to check
     * @return True if the key exists in the set of messages; false otherwise.
     */
    public boolean has(String key) {
        return messages.containsKey(key);
    }

    /**
     * @param key The key for the message to retrieve
     * @return The message corresponding to the given key, or the empty string if no such key exists.
     */
    public String get(String key) {
        return has(key) ? messages.get(key) : "";
    }

    /**
     * @return True if there are no messages stored; false otherwise
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * A factory method that creates an empty ValidationMessages object
     * @return An empty ValidationMessages object
     */
    public static ValidationMessages none() {
        return new ValidationMessages();
    }
}
