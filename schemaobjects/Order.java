package schemaobjects;
import java.sql.Date;

import enums.OrderStatus;

public class Order {
    private int        order_id;
    private int        user_id;
    private int        courier_id;

    private Date        purchase_date;
    private float       total_price;
    private OrderStatus order_status;
    private Date        receive_date;
    
    public Order(int user_id) {
        this.user_id = user_id;
        // sql to generate order id and calculate for courier id
        long ms = System.currentTimeMillis();
        this.purchase_date = new Date(ms);
        this.order_status = OrderStatus.BEINGPREPARED;
    }
    
    public void setReceiveDate(Date date) { this.receive_date = date; }
    public int getOrderID() { return this.order_id; }
    public int getUserID() { return this.user_id; }
    public int getCourierID() {
        return this.courier_id;
    }
    public Date getPurchaseDate() {
        return this.purchase_date;
    }
    public float getTotalPrice() {
        return this.total_price;
    }
    public OrderStatus getStatus() {
        return this.order_status;
    }
    public Date getReceiveDate() {
        return this.receive_date;
    }
}
