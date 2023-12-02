import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChefInterface {

    public static Scene createScene(Stage primaryStage) {
        String buttonStyle = "-fx-background-color: #cc6600; -fx-text-fill: white; -fx-font-size: 14pt; -fx-pref-width: 200px;";

        Text welcomeText = new Text("Welcome, Chef!");
        Button updateInventoryButton = new Button("Update Inventory");
        Button viewOrdersButton = new Button("View Orders");
        Button addMenuButton = new Button("Add Menu");

        welcomeText.setStyle("-fx-font-size: 20pt; -fx-fill: #cc3300; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-family: 'Georgia';");
        updateInventoryButton.setStyle(buttonStyle);
        viewOrdersButton.setStyle(buttonStyle);
        addMenuButton.setStyle(buttonStyle);

        String miniButtonStyle = "-fx-background-color: #cc6600; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-color: #924d07; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5px;" +
                "-fx-font-weight: bold;";
        String miniBack = "-fx-background-color: #ffe3c7;";

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
            
            Item.fetchAndDisplayInventoryData(newTableView);

            Button addItemButton = new Button("Add Item");
            addItemButton.setStyle(miniButtonStyle);

            addItemButton.setOnAction(event -> {
                String newItemName = itemNameField.getText();
                int newQuantity = Integer.parseInt(quantityField.getText());
        
                Item.addItemToInventory(newItemName, newQuantity);
        
                addItemStage.close(); // Close the stage after adding item
            });

        
            VBox addItemLayout = new VBox(15);
            addItemLayout.setAlignment(Pos.CENTER);
            addItemLayout.setPadding(new Insets(20));
            addItemLayout.getChildren().addAll(newTableView, itemNameField, quantityField, addItemButton); // Add new TableView to layout
            addItemLayout.setStyle(miniBack);
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();
        });
        
      

        viewOrdersButton.setOnAction(e -> {
            TableView<Item> orderItemsTableView = new TableView<>();
        
            TableColumn<Item, String> orderItemIdCol = new TableColumn<>("Order Item ID");
            orderItemIdCol.setCellValueFactory(new PropertyValueFactory<>("orderItemId"));
        
            TableColumn<Item, Integer> orderIdCol = new TableColumn<>("Order ID");
            orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        
            TableColumn<Item, String> menuItemNameCol = new TableColumn<>("Menu Item Name");
            menuItemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
            TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
            orderItemsTableView.getColumns().addAll(orderItemIdCol, orderIdCol, menuItemNameCol, quantityCol);
        
            Item.fetchOrderItemsData(orderItemsTableView);
        
            // Create a new stage to display the order items table
            VBox layout = new VBox(10);
            layout.getChildren().addAll(orderItemsTableView);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));
            layout.setStyle(miniBack);

            Scene scene = new Scene(layout, 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Order Items");
            stage.show();

            Button orderCompletedButton = new Button("Order Completed");
            orderCompletedButton.setStyle(miniButtonStyle);

            orderCompletedButton.setOnAction(cancelEvent -> {
                // Get selected reservation and cancel it
                Item selectedOrder = orderItemsTableView.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    Item.deleteOrderItem(selectedOrder.getOrderItemId());
                    Item.fetchOrderItemsData(orderItemsTableView); // Refresh table after cancellation
                }
            });
        
            layout.getChildren().add(orderCompletedButton);
        });
        

        addMenuButton.setOnAction(e -> {
            TableView<Item> tableView = new TableView<>();
            Stage addItemStage = new Stage();
            addItemStage.setTitle("Add Item to Menu");
        
            TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
            itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            TableColumn<Item, Integer> priceCol = new TableColumn<>("Price (PKR)");
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn<Item, Integer> descrCol = new TableColumn<>("Description");
            descrCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
            tableView.getColumns().addAll(itemNameCol, priceCol, descrCol); // Add columns to the new TableView
        
            TextField itemNameField = new TextField();
            itemNameField.setPromptText("Enter Item Name");
            itemNameField.setPrefColumnCount(5);

            TextField priceField = new TextField();
            priceField.setPromptText("Enter Price in PKR");
            priceField.setPrefColumnCount(5);

            TextField descField = new TextField();
            descField.setPromptText("Enter Description");
            descField.setPrefColumnCount(5);

            itemNameField.setPrefHeight(30);
            priceField.setPrefHeight(30);
            descField.setPrefHeight(50);
            
            Item.fetchAndDisplayMenuData(tableView);

            Button addItemButton = new Button("Add Item");
            addItemButton.setStyle(miniButtonStyle);

            addItemButton.setOnAction(event -> {
                String newItemName = itemNameField.getText();
                int newPrice = Integer.parseInt(priceField.getText());
                String newDesc = descField.getText();
        
                Item.addToMenu(newItemName, newPrice, newDesc);
        
                addItemStage.close(); // Close the stage after adding item
            });
            VBox addItemLayout = new VBox(15);
            addItemLayout.setAlignment(Pos.CENTER);
            addItemLayout.setPadding(new Insets(20));
            addItemLayout.setStyle(miniBack);
            addItemLayout.getChildren().addAll(tableView, itemNameField, priceField, descField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();
        });
        
        VBox chefLayout = new VBox(20);
        chefLayout.setAlignment(Pos.CENTER);
        chefLayout.getChildren().addAll(welcomeText, updateInventoryButton, viewOrdersButton, addMenuButton);

        Scene chefScene = new Scene(chefLayout, 900, 750);
        chefScene.getRoot().setStyle("-fx-background-color: ffcc66;");

        return chefScene;
    }

    // Method to fetch inventory data from MySQL and populate the TableView
    
    
}