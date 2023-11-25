import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class ChefInterface {

    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Chef!");
        Button updateInventoryButton = new Button("Update Inventory");
        Button viewOrdersButton = new Button("View Orders");
        Button addMenuButton = new Button("Add Menu");
        TableView<Item> tableView = new TableView<>();

        updateInventoryButton.setOnAction(e -> {
            Stage addItemStage = new Stage();
            addItemStage.setTitle("Add Item to Inventory");
        
            TableView<Item> newTableView = new TableView<>(); // Create a new TableView for the stage
        
            TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
            itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
            newTableView.getColumns().addAll(itemNameCol, quantityCol); // Add columns to the new TableView
        
            TextField itemNameField = new TextField();
            itemNameField.setPromptText("Enter Item Name");
            TextField quantityField = new TextField();
            quantityField.setPromptText("Enter Quantity");
        
            Button addItemButton = new Button("Add Item");
            addItemButton.setOnAction(event -> {
                String newItemName = itemNameField.getText();
                int newQuantity = Integer.parseInt(quantityField.getText()); // Assumes input is a valid integer
        
                fetchAndDisplayInventoryData(newTableView);
                addItemToInventory(newItemName, newQuantity);
                fetchAndDisplayInventoryData(newTableView); // Refresh new TableView after adding item
        
                addItemStage.close(); // Close the stage after adding item
            });
        
            VBox addItemLayout = new VBox(10);
            addItemLayout.setAlignment(Pos.CENTER);
            addItemLayout.getChildren().addAll(newTableView, itemNameField, quantityField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 500, 250);
            addItemStage.setScene(addItemScene);
            addItemStage.show();
        });
        

        VBox chefLayout = new VBox(20);
        chefLayout.setAlignment(Pos.CENTER);
        chefLayout.getChildren().addAll(welcomeText, updateInventoryButton, viewOrdersButton, addMenuButton);

        Scene chefScene = new Scene(chefLayout, 700, 500);
        chefScene.getRoot().setStyle("-fx-background-color: lightpink;");

        viewOrdersButton.setOnAction(e -> {
            // Add action for View Orders button
        });

        addMenuButton.setOnAction(e -> {
            // Add action for View Orders button
            fetchAndDisplayMenuData(tableView);
        });

        return chefScene;
    }

    // Method to fetch inventory data from MySQL and populate the TableView
    private static void fetchAndDisplayInventoryData(TableView<Item> tableView) {
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
                System.out.println(itemName + " " + quantity);
                inventoryList.add(new Item(itemName, quantity));
            }

            tableView.setItems(inventoryList);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addItemToInventory(String itemName, int quantity) {
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
    
    private static void fetchAndDisplayMenuData(TableView<Item> tableView) {
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
                // menuList.add(new Item(itemName, price, description)); // Add items to the menuList
            }
    
            tableView.setItems(menuList);
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
}