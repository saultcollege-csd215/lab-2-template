package sales.core;

public record Product(
        int id,
        String name,
        Category category,
        double price,
        int unitsInStock,
        boolean discontinued
) {

    public Product {
        if (id < 0) {
            throw new IllegalArgumentException("Product ID must be non-negative.");
        }
        if (name == null || name.trim().isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Product category is required.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price must be non-negative.");
        }
        if (unitsInStock < 0) {
            throw new IllegalArgumentException("Units in stock must be non-negative.");
        }
    }

    /**
     * A helper to return the name of a category without needing to access the category object.
     * @return The name of the product's category.
     */
    public String categoryName() {
        return category.name();
    }
}
