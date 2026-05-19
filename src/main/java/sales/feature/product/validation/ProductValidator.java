package sales.feature.product.validation;

import sales.feature.base.validation.ValidationMessages;

/**
 * A 'namespace' class for types and functions related to validating product data
 */
public class ProductValidator {

    /**
     * A 'sum type' representing whether validation passed or failed
     */
    public sealed interface Result {
        /**
         * A type for storing validated product data when validation passes
         * @param validatedProductData The validated product data
         */
        record Pass(ProductData.Validated validatedProductData) implements Result {}

        /**
         * A type for storing validation messages when validation fails
         * @param messages The validation messages
         */
        record Fail(ValidationMessages messages) implements Result {}
    }

    /**
     * Validate the given unvalidated product data
     * @param product The unvalidated product data
     * @return A Result object containing the validated product data
     */
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
