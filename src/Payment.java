/**
 * Payment
 */
public class Payment {

    public int order_id;
    public int price;
    public String status;

    public Payment(int order_id, int price, String status) {
        this.order_id = order_id;
        this.price = price;
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.order_id = orderId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return order_id;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}