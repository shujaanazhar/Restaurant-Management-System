import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ItemManager {
    public String itemName;
    public int quantity;
    public int price;
    public String description;
    private int upperLimit;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private int orderId;
    private int orderItemId;

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean isSelected) {
        this.selected.set(isSelected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public ItemManager(int order_item_id, int order_id, String itemName, int quantity) {
        this.orderItemId = order_item_id;
        this.orderId = order_id;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public ItemManager(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.upperLimit = this.quantity;
    }

    public ItemManager(String itemName2, int price, String description) {
        this.itemName = itemName2;
        this.price = price;
        this.description = description;
    }

    public void setOrderItemId(int order_item_id) {
        this.orderItemId = order_item_id;
    }

    public int getOrderItemId() {
        return this.orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperlimit) {
        this.upperLimit = upperlimit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
