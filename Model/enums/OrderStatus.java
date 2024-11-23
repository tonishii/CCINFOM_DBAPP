package Model.enums;

// OrderStatus tells if an order is still being processed, on delivery, or already delivered
public enum OrderStatus {
    BEING_PREPARED("BEING_PREPARED"),
    FOR_DELIVERY("FOR_DELIVERY"),
    DELIVERED("DELIVERED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
