package Model.enums;

// ReturnStatus gives the current status of the Request for Return which is either processing, refunded, or rejected
public enum ReturnStatus {
    PROCESSING("PROCESSING"),
    REJECTED("REJECTED"),
    REFUNDED("REFUNDED");

    private final String value;

    ReturnStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
