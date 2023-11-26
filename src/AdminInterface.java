import java.sql.Date;
import java.sql.Time;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class AdminInterface {

    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Admin!");
        Button updateInventoryButton = new Button("View Inventory");
        Button makeReservationButton = new Button("Make Reservation");
        Button addMenuButton = new Button("View Menu");

        welcomeText.setStyle("-fx-font-size: 16pt;");
        updateInventoryButton.setStyle("-fx-font-size: 14pt");
        makeReservationButton.setStyle("-fx-font-size: 14pt");
        addMenuButton.setStyle("-fx-font-size: 14pt;");

        VBox adminLayout = new VBox(20);
        adminLayout.setAlignment(Pos.CENTER);
        adminLayout.getChildren().addAll(welcomeText, updateInventoryButton, makeReservationButton, addMenuButton);

        Scene adminScene = new Scene(adminLayout, 900, 750);
        adminScene.getRoot().setStyle("-fx-background-color: lightyellow;");

        updateInventoryButton.setOnAction(e -> {
            // Add action for Update Inventory button
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
            addItemButton.setOnAction(event -> {
                String newItemName = itemNameField.getText();
                int newQuantity = Integer.parseInt(quantityField.getText());
        
                Item.addItemToInventory(newItemName, newQuantity);
        
                addItemStage.close(); // Close the stage after adding item
            });
            
            VBox addItemLayout = new VBox(15);
            addItemLayout.setAlignment(Pos.CENTER);
            addItemLayout.getChildren().addAll(newTableView, itemNameField, quantityField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();

        });

        makeReservationButton.setOnAction(e -> {
            Stage reservationStage = new Stage();
            reservationStage.setTitle("Make Reservation");
            
            TableView<Reservation> reservationTableView = new TableView<>();
            TableColumn<Reservation, Integer> reservationIdCol = new TableColumn<>("Reservation ID");
            reservationIdCol.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
            TableColumn<Reservation, String> custNameCol = new TableColumn<>("Customer Name");
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            TableColumn<Reservation, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            TableColumn<Reservation, Time> reservationTimeCol = new TableColumn<>("Reservation Time");
            reservationTimeCol.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));
            TableColumn<Reservation, Date> reservationDateCol = new TableColumn<>("Reservation Date");
            reservationDateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
            TableColumn<Reservation, Integer> numPeopleCol = new TableColumn<>("Number of People");
            numPeopleCol.setCellValueFactory(new PropertyValueFactory<>("numPeople"));

            reservationTableView.getColumns().addAll(reservationIdCol, custNameCol, emailCol, reservationTimeCol, reservationDateCol, numPeopleCol);

            // Fetch and display existing reservations
            Reservation.fetchAndDisplayReservations(reservationTableView);

            VBox reservationLayout = new VBox(15);
            reservationLayout.setAlignment(Pos.CENTER);
            reservationLayout.getChildren().addAll(reservationTableView);

            Scene reservationScene = new Scene(reservationLayout, 700, 500);
            reservationStage.setScene(reservationScene);
            reservationStage.show();

            // Button to add a new reservation
            Button addReservationButton = new Button("Add Reservation");
            addReservationButton.setOnAction(event -> {
                Stage addReservationStage = new Stage();
                addReservationStage.setTitle("Add New Reservation");

                TextField custNameField = new TextField();
                custNameField.setPromptText("Customer Name");
                TextField emailField = new TextField();
                emailField.setPromptText("Email");
                TextField reservationTimeField = new TextField();
                reservationTimeField.setPromptText("Reservation Time");
                TextField reservationDateField = new TextField();
                reservationDateField.setPromptText("Reservation Date");
                TextField numPeopleField = new TextField();
                numPeopleField.setPromptText("Number of People");

                VBox addReservationLayout = new VBox(10);
                addReservationLayout.setAlignment(Pos.CENTER);
                addReservationLayout.getChildren().addAll(custNameField, emailField, reservationTimeField,
                        reservationDateField, numPeopleField);

                Scene addReservationScene = new Scene(addReservationLayout, 400, 400);
                addReservationStage.setScene(addReservationScene);
                addReservationStage.show();

                // Button to confirm reservation addition
                Button confirmReservationButton = new Button("Confirm Reservation");
                confirmReservationButton.setOnAction(confirmEvent -> {
                    String custName = custNameField.getText();
                    String email = emailField.getText();
                    Time reservationTime = Time.valueOf(reservationTimeField.getText());
                    Date reservationDate = Date.valueOf(reservationDateField.getText());
                    int numPeople = Integer.parseInt(numPeopleField.getText());

                    Reservation.makeReservation(custName, email, reservationTime, reservationDate, numPeople);
                    Reservation.fetchAndDisplayReservations(reservationTableView);

                    addReservationStage.close();
        });

        addReservationLayout.getChildren().add(confirmReservationButton);
    });

    reservationLayout.getChildren().add(addReservationButton);
});


        addMenuButton.setOnAction(e -> {
            //Add action for Add Menu button

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
            addItemButton.setOnAction(event -> {
                String newItemName = itemNameField.getText();
                int newPrice = Integer.parseInt(priceField.getText());
                String newDesc = descField.getText();
        
                Item.addToMenu(newItemName, newPrice, newDesc);
        
                addItemStage.close(); // Close the stage after adding item
            });
            VBox addItemLayout = new VBox(15);
            addItemLayout.setAlignment(Pos.CENTER);
            addItemLayout.getChildren().addAll(tableView, itemNameField, priceField, descField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();

        });

        return adminScene;
    }
}
