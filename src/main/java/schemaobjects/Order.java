package schemaobjects;
import java.sql.Date;

import enums.OrderStatus;

public class Order {
    private long        order_id;
    private long        user_id;
    private long        courier_id;

    private Date        purchase_date;
    private float       total_price;
    private OrderStatus order_status;
    private Date        receive_date;
}
