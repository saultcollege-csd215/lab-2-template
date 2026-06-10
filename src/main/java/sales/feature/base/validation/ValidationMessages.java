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

    public static ValidationMessages of(String k1, String m1) {
        var messages = new ValidationMessages();
        messages.add(k1, m1);
        return messages;
    }
    public static ValidationMessages of(String k1, String m1, String k2, String m2) {
        var messages = of(k1, m1);
        messages.add(k2, m2);
        return messages;
    }
    public static ValidationMessages of(String k1, String m1, String k2, String m2, String k3, String m3) {
        var messages = of(k1, m1, k2, m2);
        messages.add(k3, m3);
        return messages;
    }
    public static ValidationMessages of(String k1, String m1, String k2, String m2, String k3, String m3, String k4, String m4) {
        var messages = of(k1, m1, k2, m2, k3, m3);
        messages.add(k4, m4);
        return messages;
    }
    public static ValidationMessages of(String k1, String m1, String k2, String m2, String k3, String m3, String k4, String m4, String k5, String m5) {
        var messages = of(k1, m1, k2, m2, k3, m3, k4, m4);
        messages.add(k5, m5);
        return messages;
    }

}
