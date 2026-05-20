package sales.feature.category.validation;

import sales.core.Category;

/**
 * A 'sum type' representing category data entered by the user
 */
public sealed interface CategoryData {
    /**
     * Represents user-entered category data that has not yet been validated, or is invalid in some way
     * @param name
     * @param description
     */
    record Unvalidated(String name, String description) implements CategoryData {
        public static Unvalidated of(Category c) {
            return new Unvalidated(c.name(), c.description());
        }
    }

    /**
     * Represents user-entered category data that has been validated and is thus safe to store in the database
     * @param name
     * @param description
     */
    record Validated(String name, String description) implements CategoryData {}
}
