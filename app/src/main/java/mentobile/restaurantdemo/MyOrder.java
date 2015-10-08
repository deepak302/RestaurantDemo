package mentobile.restaurantdemo;

/**
 * Created by user on 10/7/2015.
 */
public class MyOrder {

    private String order_id;
    private String order_date_time;
    private String customer_name;
    private String phone;
    private String address;
    private String order_detail;
    private String amount;
    private String status;
    private String description;

    public MyOrder(String order_id, String order_date_time, String customer_name, String phone, String address, String order_detail, String amount, String status, String description) {
        this.order_id = order_id;
        this.order_date_time = order_date_time;
        this.customer_name = customer_name;
        this.phone = phone;
        this.address = address;
        this.order_detail = order_detail;
        this.amount = amount;
        this.status = status;
        this.description = description;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(String order_detail) {
        this.order_detail = order_detail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
