package sales.core.validation;

import java.util.List;

public class CategoryValidator {

    public sealed interface Result {
        record Pass(CategoryData.Validated validatedCategoryData) implements Result {}
        record Fail(ValidationMessages messages) implements Result {}
    }

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
