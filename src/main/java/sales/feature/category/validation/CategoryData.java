package sales.feature.category.validation;

import sales.core.Category;

public sealed interface CategoryData {
    record Unvalidated(String name, String description) implements CategoryData {
        public static Unvalidated of(Category c) {
            return new Unvalidated(c.name(), c.description());
        }
    }
    record Validated(String name, String description) implements CategoryData {}
}
