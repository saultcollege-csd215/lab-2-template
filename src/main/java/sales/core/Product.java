package sales.core;

public record Product(
        int id,
        String name,
        Category category,
        double price,
        int unitsInStock,
        boolean discontinued
) {
    /**
     * A helper to return the name of a category without needing to access the category object.
     * @return The name of the product's category.
     */
    public String categoryName() {
        return category.name();
    }
}
