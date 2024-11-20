package enums;

public enum ReturnReason {
    DAMAGED("Damaged Item"),
    WRONG("Wrong Item"),
    CHANGEOFMIND("Change of Mind"),
    COUNTERFEIT("Counterfeit Item");
    
    private final String value;
    
    ReturnReason(String value) {
        this.value = value;
    }
    
    public String getVal() {
        return this.value;
    }
    
    // sql enum value to java enum value
    public static ReturnReason convertVal(String val) {
        for (ReturnReason reason : values()) {
            if (reason.value.equals(val))
                return reason;
        }
        throw new IllegalArgumentException("Unknown return reason: " + val);
    }
    
}
