public class Item {
    private String itemName;
    private int quantity;
    private int price;
    private String description;


    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // public Item(String itemName2, int price, String description) {
    //     this.itemName = itemName2;
    //     this.price = price;
    //     this.description = description;
    // }

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
}
