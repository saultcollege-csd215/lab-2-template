package sales.feature.product.validation;

import sales.feature.base.validation.ValidationMessages;

public class ProductValidator {

    public sealed interface Result {
        record Pass(ProductData.Validated validatedProductData) implements Result {}
        record Fail(ValidationMessages messages) implements Result {}
    }

    public static Result validate(ProductData.Unvalidated product) {
        String name = product.name();
        var messages = new ValidationMessages();
        if (name.trim().isBlank()) {
            messages.add("name", "Product name is required.");
        }

        if (product.categoryId() <= 0) {
            messages.add("categoryId", "Category must be set.");
        }

        if (product.price() < 0) {
            messages.add("price", "Product price must be non-negative.");
        }

        if (product.unitsInStock() < 0) {
            messages.add("unitsInStock", "Units in stock must be non-negative.");
        }

        if ( messages.isEmpty() ) {
            return new Result.Pass(new ProductData.Validated(
                    name.trim(),
                    product.categoryId(),
                    product.price(),
                    product.unitsInStock(),
                    product.discontinued()
            ));
        } else {
            return new Result.Fail(messages);
        }

    }
}
