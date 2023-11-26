import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;

public class Item {
    public String itemName;
    public int quantity;
    public int price;
    public String description;


    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Item(String itemName2, int price, String description) {
        this.itemName = itemName2;
        this.price = price;
        this.description = description;
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
            ex.printStackTrace();
        }
    }
    
    public static void addToMenu(String itemName, int price, String description) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // Check if the item already exists in the menu
            PreparedStatement checkIfExists = connection.prepareStatement("SELECT item_name FROM menu WHERE item_name = ?");
            checkIfExists.setString(1, itemName);
            ResultSet existingItem = checkIfExists.executeQuery();
    
            if (!existingItem.next()) {
                // If the item doesn't exist, proceed to insert it into the menu
                PreparedStatement insertItem = connection.prepareStatement("INSERT INTO menu (item_name, price, descrip) VALUES (?, ?, ?)");
                insertItem.setString(1, itemName);
                insertItem.setInt(2, price);
                insertItem.setString(3, description);
                insertItem.executeUpdate();
    
                insertItem.close();
            } else {
                System.out.println("Item already exists in the menu.");
            }
    
            existingItem.close();
            checkIfExists.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
