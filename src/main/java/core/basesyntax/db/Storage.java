package core.basesyntax.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static final Map<String, Integer> storage = new HashMap<>();

    public static void setQuantity(String fruit, int quantity) {
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative: " + quantity);
        }
        storage.put(fruit, quantity);
    }

    public static void mergeQuantity(String fruit, int delta) {
        storage.merge(fruit, delta, Integer::sum);
    }

    public static int getQuantity(String fruit) {
        return storage.getOrDefault(fruit, 0);
    }

    public static Map<String, Integer> getAll() {
        return Collections.unmodifiableMap(storage);
    }

    public static void clear() {
        storage.clear();
    }

    public static Map<String, Integer> getFruits() {
        return Collections.unmodifiableMap(storage);
    }
}
