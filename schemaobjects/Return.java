package schemaobjects;
import java.sql.Date;

import enums.ReturnReason;
import enums.ReturnStatus;

public class Return {
    private long          order_id;
    private long          product_id;
    private long          courier_id;

    private ReturnReason  return_reason;
    private String        return_description;
    private Date          return_date;
    private ReturnStatus  return_status;
    
    public Return(long order_id, long product_id, ReturnReason return_reason, String return_description) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.return_reason = return_reason;
        this.return_description = return_description;
        long ms = System.currentTimeMillis();
        this.return_date = new Date(ms);
        this.return_status = ReturnStatus.PROCESSING;
        // sql to generate courier id
    }
    
    public long getOrderID() {
        return this.order_id;
    }
    public long getProductID() {
        return this.product_id;
    }
    public long getCourierID() {
        return this.courier_id;
    }
    public ReturnReason getReason() {
        return this.return_reason;
    }
    public String getDescription() {
        return this.return_description;
    }
    public Date getDate() {
        return this.return_date;
    }
    public ReturnStatus getStatus() {
        return this.return_status;
    }
}
