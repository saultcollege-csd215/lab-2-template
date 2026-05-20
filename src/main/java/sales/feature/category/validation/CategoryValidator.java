package sales.feature.category.validation;

import sales.feature.base.validation.ValidationMessages;

import java.util.List;

/**
 * A 'namespace' class for types and functions related to validating Category data
 */
public class CategoryValidator {

    /**
     * Represents the result of category validation, either 'pass' or 'fail'
     */
    public sealed interface Result {
        /**
         * If validation passes, the result includes the validated category data
         * @param validatedCategoryData The validated category data
         */
        record Pass(CategoryData.Validated validatedCategoryData) implements Result {}

        /**
         * If validation fails, the result includes the validation messages
         * @param messages The validation messages
         */
        record Fail(ValidationMessages messages) implements Result {}
    }

    /**
     * Returns a Result object indicating whether the given unvalidated category data is valid or not
     * @param c The unvalidated category data to validate
     * @param existingNames A list of existing category names (to check for duplicate category names)
     * @return Either a Pass(CategoryData.Validated) or a Fail(ValidationMessages)
     */
    public static Result validate(CategoryData.Unvalidated c, List<String> existingNames) {
        var messages = new ValidationMessages();

        if (c.name().isBlank()) {
            messages.add("name", "Name is required.");
        } else if (existingNames.contains(c.name())) {
            messages.add("name", "Name must be unique.");
        }

        if (c.description().isBlank()) {
            messages.add("description", "Description is required.");
        } else if (c.description().length() > 500) {
            messages.add("description", "Description must be less than 500 characters.");
        }

        if ( messages.isEmpty() ) {
            return new Result.Pass(new CategoryData.Validated(c.name(), c.description()));
        } else {
            return new Result.Fail(messages);
        }
    }
}
