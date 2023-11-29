import java.sql.*;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;



public class CustomerInterface {
    
    public static String email;
    
    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Customer!");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email as ab c@xyz.com");

        Button makeReservationButton = new Button("Reserve Table");
        
        Button placeOrderButton = new Button("Place Order");
        
        Button cancelReservationButton = new Button("Cancel Reservation");
        
        Button makePaymentButton = new Button("Make Payment");

        welcomeText.setStyle("-fx-font-size: 16pt;");
        makeReservationButton.setStyle("-fx-font-size: 14pt;");
        cancelReservationButton.setStyle("-fx-font-size: 14pt;");
        placeOrderButton.setStyle("-fx-font-size: 14pt;");
        makePaymentButton.setStyle("-fx-font-size: 14pt;");

        emailField.setPrefWidth(200);
        emailField.setMinWidth(200);
        emailField.setMaxWidth(200);
        emailField.setAlignment(Pos.CENTER);
        // UnaryOperator<TextFormatter.Change> filter = change -> {
        //     if (change.isAdded() && change.getControlNewText().trim().isEmpty()) {
        //         return null; // Disallow adding whitespace at the start or end
        //     }
        //     return change;
        // };
        // TextFormatter<String> formatter = new TextFormatter<>(filter);
        // emailField.setTextFormatter(formatter);


        VBox customerLayout = new VBox(20);
        customerLayout.setAlignment(Pos.CENTER);
        customerLayout.getChildren().addAll(welcomeText, emailField, makeReservationButton, cancelReservationButton, placeOrderButton, makePaymentButton);

        Scene customerScene = new Scene(customerLayout, 900, 750);
        customerScene.getRoot().setStyle("-fx-background-color: lightgreen;");

        makeReservationButton.setOnAction(e -> {
            email = emailField.getText();
            if (email.isEmpty()) {
            showAlert("Email field cannot be empty.");
            }
            else{
            Stage addReservationStage = new Stage();
            addReservationStage.setTitle("Add New Reservation");

            TextField custNameField = new TextField();
            custNameField.setPromptText("Customer Name");
            

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
            addReservationLayout.getChildren().addAll(custNameField, datePicker, timeBox, numPeopleField);

            Scene addReservationScene = new Scene(addReservationLayout, 400, 400);
            addReservationStage.setScene(addReservationScene);
            addReservationStage.show();

            // Button to confirm reservation addition
            Button confirmReservationButton = new Button("Confirm Reservation");
            confirmReservationButton.setOnAction(confirmEvent -> {
                String custName = custNameField.getText();

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

                // Show confirmation pop-up
                Reservation.showReservationMadePopup();
            });

            addReservationLayout.getChildren().add(confirmReservationButton);
            }
        });


        cancelReservationButton.setOnAction(e -> {
            email = emailField.getText();
            if (email.isEmpty()) {
            showAlert("Email field cannot be empty.");
            }
            else{
            // Stage cancelReservationStage = new Stage();
            // cancelReservationStage.setTitle("Cancel Reservation");

            // Button confirmCancelReservationButton = new Button("Confirm Cancel");
            // confirmCancelReservationButton.setOnAction(event -> {

                Reservation.cancelReservation(email);
                Reservation.showReservationCancelledPopup();
                // cancelReservationStage.close();
            }
            });

            // VBox cancelReservationLayout = new VBox(10);
            // cancelReservationLayout.setAlignment(Pos.CENTER);
            // cancelReservationLayout.getChildren().addAll(confirmCancelReservationButton);

            // Scene cancelReservationScene = new Scene(cancelReservationLayout, 300, 200);
            // cancelReservationStage.setScene(cancelReservationScene);
            // cancelReservationStage.show();
        // });

        placeOrderButton.setOnAction(e -> {
            email = emailField.getText();
            if (email.isEmpty()) {
            showAlert("Email field cannot be empty.");
            }
            else{
                if (isValidReservation(email)) {
                TableView<Item> menuTableView = new TableView<>();
                TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
                itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

                TableColumn<Item, String> priceCol = new TableColumn<>("Price");
                priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

                TableColumn<Item, String> descCol = new TableColumn<>("Description");
                descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            
                TableColumn<Item, Boolean> selectCol = new TableColumn<>("Select");
                selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
                selectCol.setCellFactory(col -> new CheckBoxCell());
            
                TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
                quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            
                // Buttons to adjust quantity
                TableColumn<Item, Void> quantityButtonsCol = new TableColumn<>("Adjust Quantity");
                Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {
                    @Override
                    public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
                        final TableCell<Item, Void> cell = new TableCell<>() {
                            private final Button addButton = new Button("+");
                            private final Button subtractButton = new Button("-");
            
                            {
                                addButton.setOnAction(event -> {
                                    Item item = getTableView().getItems().get(getIndex());
                                    int newQuantity = item.getQuantity() - 1;
                                    if (newQuantity >= 0) {
                                        item.setQuantity(newQuantity);
                                        getTableView().refresh();
                                    }
                                });
            
                                subtractButton.setOnAction(event -> {
                                    Item item = getTableView().getItems().get(getIndex());
                                    int currentQuantity = item.getQuantity();
                                    int upperLimit = item.getUpperLimit();

                                    if (currentQuantity < upperLimit) {
                                        item.setQuantity(Math.min(currentQuantity + 1, upperLimit));
                                        getTableView().refresh();
                                    }
                                });
                            }
            
                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    HBox buttons = new HBox(addButton, subtractButton);
                                    buttons.setSpacing(5);
                                    setGraphic(buttons);
                                }
                            }
                        };
                        return cell;
                    }
                };
            
                quantityButtonsCol.setCellFactory(cellFactory);
            
                menuTableView.getColumns().addAll(itemNameCol, priceCol, descCol, selectCol, quantityCol, quantityButtonsCol);
                Item.fetchAvailableMenuItems(menuTableView); // Fetch and display available menu items
            
                // Button to proceed to order confirmation
                Button confirmOrderButton = new Button("Confirm Order");
                confirmOrderButton.setOnAction(confirmEvent -> {
                    ObservableList<Item> selectedItems = menuTableView.getItems().stream()
                            .filter(Item::isSelected)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));

                    if (!selectedItems.isEmpty()) {
                        Item.processSelectedItems(selectedItems);
                        Item.showOrderConfirmation();
                    } else {
                        showAlert("No items selected for order.");
                    }
                });
            
                VBox menuLayout = new VBox(10);
                menuLayout.setAlignment(Pos.CENTER);
                menuLayout.getChildren().addAll(menuTableView, confirmOrderButton);
            
                Scene menuScene = new Scene(menuLayout, 800, 600);
                Stage menuStage = new Stage();
                menuStage.setTitle("Select Menu Items");
                menuStage.setScene(menuScene);
                menuStage.show();
            }
        }
        });
        

        


        makePaymentButton.setOnAction(e -> {
            email = emailField.getText();
            if (email.isEmpty()) {
            showAlert("Email field cannot be empty.");
            }
            else{

            }

        });

        return customerScene;
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isValidReservation(String email) {
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD)) {
            String query = "SELECT * FROM reservation WHERE email = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If there's at least one row, the email exists in the reservation table
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any exception or database error
        }
    }
    

}
