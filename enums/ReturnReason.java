package enums;

public enum ReturnReason {
    DAMAGED("Damaged Item"),
    WRONG("Wrong Item"),
    CHANGEOFMIND("Change of Mind"),
    COUNTERFEIT("Counterfeit Item");
    
    private String val;
    
    ReturnReason(String val) {
        this.val = val;
    }
    
    public String getVal() {
        return this.val;
    }
    
    // sql enum value to java enum value
    public static ReturnReason convertVal(String val) {
        for (ReturnReason reason : values()) {
            if (reason.val.equals(val))
                return reason;
        }
        throw new IllegalArgumentException("Unknown return reason: " + val);
    }
    
}
