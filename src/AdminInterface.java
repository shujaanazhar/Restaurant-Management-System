import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminInterface {

    public static Scene createScene(Stage primaryStage) {
        String buttonStyle = "-fx-background-color: #610000; -fx-text-fill: white; -fx-font-size: 14pt; -fx-pref-width: 200px;";

        Text welcomeText = new Text("Welcome, Admin!");
        Button updateInventoryButton = new Button("View Inventory");
        Button makeReservationButton = new Button("Make Reservation");
        Button cancelReservationButton = new Button("Cancel Reservation");
        Button addMenuButton = new Button("View Menu");
        

        welcomeText.setStyle("-fx-font-size: 20pt; -fx-fill: #610000; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-family: 'Georgia';");
        updateInventoryButton.setStyle(buttonStyle);
        makeReservationButton.setStyle(buttonStyle);
        cancelReservationButton.setStyle(buttonStyle);
        addMenuButton.setStyle(buttonStyle);
        String miniButtonStyle = "-fx-background-color: #750000; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-color: #800000; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5px;" +
                "-fx-font-weight: bold;";

        String miniBack = "-fx-background-color: #ffc2c2;";

        VBox adminLayout = new VBox(20);
        adminLayout.setAlignment(Pos.CENTER);
        adminLayout.getChildren().addAll(welcomeText, updateInventoryButton, makeReservationButton, cancelReservationButton, addMenuButton);

        Scene adminScene = new Scene(adminLayout, 900, 750);
        adminLayout.setStyle("-fx-background-color: #ffcccc; -fx-padding: 50px; -fx-spacing: 20px;");

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
            addItemLayout.setStyle(miniBack);
            addItemLayout.getChildren().addAll(newTableView, itemNameField, quantityField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();

        });

        makeReservationButton.setOnAction(e -> {
            Stage reservationStage = new Stage();
            reservationStage.setTitle("Make Reservation");
            
            TableView<Reservation> reservationTableView = new TableView<>();
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

            reservationTableView.getColumns().addAll(custNameCol, emailCol, reservationTimeCol, reservationDateCol, numPeopleCol);

            // Fetch and display existing reservations
            Reservation.fetchAndDisplayReservations(reservationTableView);

            VBox reservationLayout = new VBox(15);
            reservationLayout.setAlignment(Pos.CENTER);
            reservationLayout.setPadding(new Insets(20));
            reservationLayout.setStyle(miniBack);
            reservationLayout.getChildren().addAll(reservationTableView);

            Scene reservationScene = new Scene(reservationLayout, 700, 500);
            reservationStage.setScene(reservationScene);
            reservationStage.show();

            // Button to add a new reservation
            Button addReservationButton = new Button("Add Reservation");
            addReservationButton.setStyle(miniButtonStyle);

            addReservationButton.setOnAction(event -> {
                Stage addReservationStage = new Stage();
                addReservationStage.setTitle("Add New Reservation");

                TextField custNameField = new TextField();
                custNameField.setPromptText("Customer Name");
                TextField emailField = new TextField();
                emailField.setPromptText("Email");

                // DatePicker for reservation date
                DatePicker datePicker = new DatePicker();
                datePicker.setPromptText("Reservation Date");

                // Spinners for reservation time (hours and minutes)
                Spinner<Integer> hoursSpinner = new Spinner<>(0, 23, 12);
                Spinner<Integer> minutesSpinner = new Spinner<>(0, 59, 0);
                Label timeLabel = new Label("Reservation Time:");
                HBox timeBox = new HBox(10, timeLabel, hoursSpinner, new Label(":"), minutesSpinner);

                TextField numPeopleField = new TextField();
                numPeopleField.setPromptText("Number of People");

                VBox addReservationLayout = new VBox(10);
                addReservationLayout.setAlignment(Pos.CENTER);
                addReservationLayout.setPadding(new Insets(20));;
                addReservationLayout.getChildren().addAll(custNameField, emailField, datePicker, timeBox, numPeopleField);

                Scene addReservationScene = new Scene(addReservationLayout, 400, 400);
                addReservationStage.setScene(addReservationScene);
                addReservationStage.show();

                // Button to confirm reservation addition
                Button confirmReservationButton = new Button("Confirm Reservation");
                confirmReservationButton.setStyle(miniButtonStyle);

                confirmReservationButton.setOnAction(confirmEvent -> {
                    String custName = custNameField.getText();
                    String email = emailField.getText();

                    // Get selected date from DatePicker
                    LocalDate selectedDate = datePicker.getValue();
                    int hours = hoursSpinner.getValue();
                    int minutes = minutesSpinner.getValue();

                    // Create Date and Time objects from selected values
                    Date reservationDate = Date.valueOf(selectedDate);
                    Time reservationTime = Time.valueOf(String.format("%02d:%02d:00", hours, minutes));

                    int numPeople = Integer.parseInt(numPeopleField.getText());

                    // Make reservation using the entered details
                    Reservation.makeReservation(custName, email, reservationTime, reservationDate, numPeople);
                    Reservation.fetchAndDisplayReservations(reservationTableView);

                    addReservationStage.close();
                });
                

                addReservationLayout.getChildren().add(confirmReservationButton);
                addReservationLayout.setStyle(miniBack);
            });
            reservationLayout.getChildren().add(addReservationButton);
        });

        cancelReservationButton.setOnAction(event -> {
            Stage cancelReservationStage = new Stage();
            cancelReservationStage.setTitle("Cancel Reservation");
        
            TableView<Reservation> cancelReservationTableView = new TableView<>();
            TableColumn<Reservation, String> custNameCol = new TableColumn<>("Customer Name");
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            TableColumn<Reservation, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            TableColumn<Reservation, String> resTimeCol = new TableColumn<>("Reservation Time");
            resTimeCol.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));
            TableColumn<Reservation, String> resDateCol = new TableColumn<>("Reservation Date");
            resDateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
            TableColumn<Reservation, String> numPeopleCol = new TableColumn<>("Number of People");
            numPeopleCol.setCellValueFactory(new PropertyValueFactory<>("numPeople"));
            
        
            cancelReservationTableView.getColumns().addAll(custNameCol, emailCol, resTimeCol, resDateCol, numPeopleCol); // Add necessary columns
        
            // Fetch and display existing reservations
            Reservation.fetchAndDisplayReservations(cancelReservationTableView);
        
            VBox cancelReservationLayout = new VBox(15);
            cancelReservationLayout.setAlignment(Pos.CENTER);
            cancelReservationLayout.setPadding(new Insets(20));
            cancelReservationLayout.setStyle(miniBack);
            cancelReservationLayout.getChildren().addAll(cancelReservationTableView);
        
            Scene cancelReservationScene = new Scene(cancelReservationLayout, 700, 500);
            cancelReservationStage.setScene(cancelReservationScene);
            cancelReservationStage.show();
        
            // Button to cancel a reservation
            Button cancelSelectedReservationButton = new Button("Cancel Selected Reservation");
            cancelSelectedReservationButton.setStyle(miniButtonStyle);

            cancelSelectedReservationButton.setOnAction(cancelEvent -> {
                // Get selected reservation and cancel it
                Reservation selectedReservation = cancelReservationTableView.getSelectionModel().getSelectedItem();
                if (selectedReservation != null) {
                    Reservation.cancelReservation(selectedReservation.getEmail());
                    Reservation.fetchAndDisplayReservations(cancelReservationTableView); // Refresh table after cancellation
                }
            });
        
            cancelReservationLayout.getChildren().add(cancelSelectedReservationButton);
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
            addItemLayout.getChildren().addAll(tableView, itemNameField, priceField, descField, addItemButton); // Add new TableView to layout
        
            Scene addItemScene = new Scene(addItemLayout, 700, 500);
            addItemStage.setScene(addItemScene);
            addItemStage.show();

        });

        return adminScene;
    }
}
