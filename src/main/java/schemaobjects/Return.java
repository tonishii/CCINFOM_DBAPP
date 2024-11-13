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
}
