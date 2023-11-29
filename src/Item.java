import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.*;

public class Item {
    public String itemName;
    public int quantity;
    public int price;
    public String description;
    private int upperLimit;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean isSelected) {
        this.selected.set(isSelected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.upperLimit = this.quantity;
    }

    public Item(String itemName2, int price, String description) {
        this.itemName = itemName2;
        this.price = price;
        this.description = description;
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
    public static void fetchAndDisplayInventoryData(TableView<Item> tableView) {
        try {

            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();

            // Query to select data from the inventory table
            String query = "SELECT * FROM inventory";
            ResultSet resultSet = statement.executeQuery(query);

            // Populate the TableView with retrieved data
            ObservableList<Item> inventoryList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");
                
                // // Fetching additional details from the menu table using JOIN
                // String menuQuery = "SELECT * FROM menu WHERE item_name = '" + itemName + "'";
                // ResultSet menuResultSet = statement.executeQuery(menuQuery);
                // if (menuResultSet.next()) {
                //     int price = menuResultSet.getInt("price");
                //     String description = menuResultSet.getString("descrip");
                    inventoryList.add(new Item(itemName, quantity));
                // }
                // menuResultSet.close();
            }

            tableView.setItems(inventoryList);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Unable to fetch Inventory Data!");
            e.printStackTrace();
        }
    }

    public static void addItemToInventory(String itemName, int quantity) {
        try {
            // Your database connection details
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
    
            // Query to insert a new item into the inventory table
            String insertOrUpdateQuery = "INSERT INTO inventory (item_name, quantity) VALUES ('" + itemName + "', " + quantity +
                ") ON DUPLICATE KEY UPDATE quantity = quantity + " + quantity;
            statement.executeUpdate(insertOrUpdateQuery);
    
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to Update the Inventory!");
            ex.printStackTrace();
        }
    }
    
    public static void fetchAndDisplayMenuData(TableView<Item> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
    
            String query = "SELECT * FROM menu";
            ResultSet resultSet = statement.executeQuery(query);
    
            ObservableList<Item> menuList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("descrip");
                menuList.add(new Item(itemName, price, description)); // Add items to the menuList
            }
    
            tableView.setItems(menuList);
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to fetch Menu Data");
            ex.printStackTrace();
        }
    }
    
    public static void addToMenu(String itemName, int price, String description) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // // Check if the item already exists in the menu
            // PreparedStatement checkIfExists = connection.prepareStatement("SELECT item_name FROM menu WHERE item_name = ?");
            // checkIfExists.setString(1, itemName);
            // ResultSet existingItem = checkIfExists.executeQuery();
    
            // if (!existingItem.next()) {
                // If the item doesn't exist, proceed to insert it into the menu
                PreparedStatement insertItem = connection.prepareStatement("INSERT INTO menu (item_name, price, descrip) VALUES (?, ?, ?)");
                insertItem.setString(1, itemName);
                insertItem.setInt(2, price);
                insertItem.setString(3, description);
                insertItem.executeUpdate();
    
                insertItem.close();
            // } else {
                // System.out.println("Item already exists in the menu.");
            // }
    
            // existingItem.close();
            // checkIfExists.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Menu item already exists!");
            e.printStackTrace();
        }
    }

    public static void fetchAvailableMenuItems(TableView<Item> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM inventory JOIN menu ON inventory.item_name = menu.item_name WHERE inventory.quantity > 0";
            ResultSet resultSet = statement.executeQuery(query);

            ObservableList<Item> menuList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("menu.item_name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("descrip");
                int quantity = resultSet.getInt("quantity");

                // Create an Item object with fetched data
                Item item = new Item(itemName, quantity);
                item.setPrice(price);
                item.setDescription(description);

                menuList.add(item); // Add items to the menuList
            }

            tableView.setItems(menuList);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to fetch Menu items!");
            ex.printStackTrace();
        }
    }

    public static void processSelectedItems(ObservableList<Item> selectedItems) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // Generate a unique order ID (you might need a proper ID generator in practice)
            int orderID = generateUniqueOrderID(connection);
    
            // Insert into the 'orders' table
            PreparedStatement insertOrderStmt = connection.prepareStatement("INSERT INTO orders (order_id, reservation_id) VALUES (?, ?)");
            insertOrderStmt.setInt(1, orderID);
            // Assuming reservation_id is hard-coded here; in reality, it should come from a specific reservation
            if(CustomerInterface.isValidReservation(CustomerInterface.email)) {
                insertOrderStmt.setString(2, CustomerInterface.email); // Replace this with the actual reservation ID
            }
            insertOrderStmt.executeUpdate();
            insertOrderStmt.close();
    
            // Insert each selected item into the 'order_items' table
            PreparedStatement insertOrderItemsStmt = connection.prepareStatement("INSERT INTO order_items (order_id, menu_item_name, quantity) VALUES (?, ?, ?)");
            for (Item selectedItem : selectedItems) {
                insertOrderItemsStmt.setInt(1, orderID);
                insertOrderItemsStmt.setString(2, selectedItem.getItemName());
                // Assuming a default quantity of 1; you can change this based on user input
                PreparedStatement quantityStmt = connection.prepareStatement("SELECT quantity FROM inventory WHERE item_name = ?");
                quantityStmt.setString(1, selectedItem.getItemName());

                ResultSet resultSet = quantityStmt.executeQuery();
                int invent_Quantity = 0; // Default value if no quantity is found
                if (resultSet.next()) {
                    invent_Quantity = resultSet.getInt("quantity");
                }

                // Update the quantity in the order_items table by subtracting the selected quantity
                int updatedQuantity = invent_Quantity - selectedItem.getQuantity();
                if (updatedQuantity >= 0) {
                    insertOrderItemsStmt.setInt(3, updatedQuantity);
                    insertOrderItemsStmt.executeUpdate();
                    PreparedStatement updateInventoryStmt = connection.prepareStatement("UPDATE inventory SET quantity = ? WHERE item_name = ?");
                    updateInventoryStmt.setInt(1, selectedItem.getQuantity());
                    updateInventoryStmt.setString(2, selectedItem.getItemName());
                    updateInventoryStmt.executeUpdate();
                }

                /*JUST NOW UPDATE THE QUANTITY BY SUBTRACTING IT IN THE INVENTORY */
            }
            insertOrderItemsStmt.close();
    
            connection.close();
            
            // Display a success message or perform any other necessary actions here
            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            showAlert("Unable to place your order. Please order again!");
            e.printStackTrace();
        }
    }

    private static int generateUniqueOrderID(Connection connection) throws SQLException {
        int orderID = 0;
        PreparedStatement stmt = connection.prepareStatement("SELECT MAX(order_id) FROM orders");
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            orderID = resultSet.getInt(1) + 1;
        }
        resultSet.close();
        stmt.close();
        return orderID;
    }

    public static void showOrderConfirmation() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();
    }
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
