import java.sql.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;



public class CustomerInterface {
    
    public static String email;
    
    public static Scene createScene(Stage primaryStage) {
        String buttonStyle = "-fx-background-color: #C46B02; -fx-text-fill: white; -fx-font-size: 14pt; -fx-pref-width: 200px;";

        Text welcomeText = new Text("Welcome, Customer!");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email as abc@xyz.com");

        Button makeReservationButton = new Button("Reserve Table");
        
        Button placeOrderButton = new Button("Place Order");
        
        Button cancelReservationButton = new Button("Cancel Reservation");
        
        Button makePaymentButton = new Button("Make Payment");

        welcomeText.setStyle("-fx-font-size: 20pt; -fx-fill: #ff6600; -fx-font-weight: bold; -fx-font-style: italic; -fx-font-family: 'Georgia';");

        makeReservationButton.setStyle(buttonStyle);
        cancelReservationButton.setStyle(buttonStyle);
        placeOrderButton.setStyle(buttonStyle);
        makePaymentButton.setStyle(buttonStyle);

        emailField.setPrefWidth(200);
        emailField.setMinWidth(200);
        emailField.setMaxWidth(200);
        emailField.setAlignment(Pos.CENTER);

        VBox customerLayout = new VBox(20);
        customerLayout.setAlignment(Pos.CENTER);
        customerLayout.getChildren().addAll(welcomeText, emailField, makeReservationButton, cancelReservationButton, placeOrderButton, makePaymentButton);

        Scene customerScene = new Scene(customerLayout, 900, 750);
        customerLayout.setStyle("-fx-background-color: #FAD5C2; -fx-padding: 50px; -fx-spacing: 20px;");

        String miniButtonStyle = "-fx-background-color: #f4711a; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-color: #ff6600; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5px;";
        
        String miniBack = "-fx-background-color: ffdbb8;";

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

            // Create HBox for time elements
            HBox timeBox = new HBox(10);
            timeBox.setAlignment(Pos.CENTER);
            Spinner<Integer> hoursSpinner = new Spinner<>(0, 23, 12);
            Spinner<Integer> minutesSpinner = new Spinner<>(0, 59, 0);
            timeBox.getChildren().addAll(hoursSpinner, new Label(":"), minutesSpinner);

            TextField numPeopleField = new TextField();
            numPeopleField.setPromptText("Number of People");
            
            // Add labels and fields with proper spacing
            Label nameLabel = new Label("Customer Name:");
            Label dateLabel = new Label("Reservation Date:");
            Label timeLabel = new Label("Reservation Time (hh/mm):");
            Label numPeopleLabel = new Label("Number of People:");

            VBox addReservationLayout = new VBox(15);
            addReservationLayout.setAlignment(Pos.CENTER);
            addReservationLayout.setPadding(new Insets(20));
            addReservationLayout.setStyle(miniBack);
            addReservationLayout.getChildren().addAll(nameLabel, custNameField, dateLabel, datePicker, timeLabel, timeBox, numPeopleLabel, numPeopleField);

            // Apply styles
            nameLabel.setStyle("-fx-font-weight: bold;");
            dateLabel.setStyle("-fx-font-weight: bold;");
            timeLabel.setStyle("-fx-font-weight: bold;");
            numPeopleLabel.setStyle("-fx-font-weight: bold;");
            custNameField.setStyle("-fx-pref-width: 200;");
            numPeopleField.setStyle("-fx-pref-width: 200;");

            Scene addReservationScene = new Scene(addReservationLayout, 400, 400);
            addReservationStage.setScene(addReservationScene);
            addReservationStage.show();

            Button confirmReservationButton = new Button("Confirm Reservation");

            confirmReservationButton.setStyle(miniButtonStyle);

            confirmReservationButton.setOnMouseEntered(so -> {
                confirmReservationButton.setStyle(miniButtonStyle + "-fx-background-color: #f99858;");
            });

            confirmReservationButton.setOnMouseExited(so -> {
                confirmReservationButton.setStyle(miniButtonStyle);
            });
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
                addReservationStage.close();
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
                if (isValidReservation(email)) {
                Reservation.cancelReservation(email);
                Reservation.showReservationCancelledPopup();
                }
                else {
                    showAlert("No Customer Data with this email found!");
                }
            }
        });

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
                                    int currentQuantity = item.getQuantity();
                                    int upperLimit = item.getUpperLimit();

                                    if (currentQuantity < upperLimit) {
                                        item.setQuantity(Math.min(currentQuantity + 1, upperLimit));
                                        getTableView().refresh();
                                    }
                                });
            
                                subtractButton.setOnAction(event -> {
                                    Item item = getTableView().getItems().get(getIndex());
                                    int newQuantity = item.getQuantity() - 1;
                                    if (newQuantity >= 0) {
                                        item.setQuantity(newQuantity);
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
                confirmOrderButton.setStyle(miniButtonStyle);
                confirmOrderButton.setOnMouseEntered(so -> {
                    confirmOrderButton.setStyle(miniButtonStyle + "-fx-background-color: #f99858;");
                });

                    confirmOrderButton.setOnMouseExited(so -> {
                        confirmOrderButton.setStyle(miniButtonStyle);
                });
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
                menuLayout.setStyle(miniBack);
            
                Scene menuScene = new Scene(menuLayout, 800, 600);
                Stage menuStage = new Stage();
                menuStage.setTitle("Select Menu Items");
                menuStage.setScene(menuScene);
                menuStage.show();
            }
            else {
                    showAlert("Unable to Order as No Reservation Found!");
                }
        }
        });
        
        makePaymentButton.setOnAction(e -> {
            email = emailField.getText();
            if (email.isEmpty()) {
                showAlert("Email field cannot be empty.");
            } else {
                // Stage for Payment Details
                Stage paymentStage = new Stage();
                paymentStage.setTitle("Payment Details");
        
                // Payment TableView and Columns
                TableView<Payment> paymentTableView = new TableView<>();
                TableColumn<Payment, Integer> orderIDCol = new TableColumn<>("Order ID");
                orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
                TableColumn<Payment, Integer> totalPriceCol = new TableColumn<>("Total Price");
                totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
                TableColumn<Payment, String> paymentStatusCol = new TableColumn<>("Payment Status");
                paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
                paymentTableView.getColumns().addAll(orderIDCol, totalPriceCol, paymentStatusCol);
        
                // Fetch and display payment information
                Item.fetchPaymentInformation(paymentTableView, email);
        
                // Complete Payment Button
                Button completePaymentButton = new Button("Complete Payment");
                completePaymentButton.setStyle(miniButtonStyle);
                completePaymentButton.setOnMouseEntered(so -> {
                    completePaymentButton.setStyle(miniButtonStyle + "-fx-background-color: #f99858;");
                });

                    completePaymentButton.setOnMouseExited(so -> {
                        completePaymentButton.setStyle(miniButtonStyle);
                });
                completePaymentButton.setAlignment(Pos.CENTER);
        
                completePaymentButton.setOnAction(cancelEvent -> {
                    // Get selected payment and update its status to 'paid'
                    Payment selectedPayment = paymentTableView.getSelectionModel().getSelectedItem();
                    if (selectedPayment != null) {
                        Item.updatePaymentStatusToPaid(selectedPayment.getOrderId(), email);
        
                        Stage cardDetailsStage = new Stage();
                        cardDetailsStage.setTitle("Enter Card Details");

                        Label cardNumberLabel = new Label("Card Number:");
                        TextField cardNumberField = new TextField();
                        Label expiryLabel = new Label("Expiry Date:");
                        TextField expiryField = new TextField();
                        Label cvvLabel = new Label("CVV:");
                        TextField cvvField = new TextField();

                        Button confirmButton = new Button("Confirm");
                        confirmButton.setStyle(miniButtonStyle);
                        confirmButton.setOnMouseEntered(so -> {
                            confirmButton.setStyle(miniButtonStyle + "-fx-background-color: #f99858;");
                        });

                            confirmButton.setOnMouseExited(so -> {
                                confirmButton.setStyle(miniButtonStyle);
                        });
                        confirmButton.setAlignment(Pos.CENTER);

                        // Apply styles to the elements
                        cardNumberLabel.setStyle("-fx-font-weight: bold;");
                        expiryLabel.setStyle("-fx-font-weight: bold;");
                        cvvLabel.setStyle("-fx-font-weight: bold;");

                        // Layout setup
                        VBox root = new VBox(10);
                        root.setPadding(new Insets(20));
                        root.setAlignment(Pos.CENTER);
                        root.getChildren().addAll(cardNumberLabel, cardNumberField, expiryLabel, expiryField, cvvLabel, cvvField, confirmButton);
        
                        // Confirm Payment
                        confirmButton.setOnAction(event -> {
                            Item.fetchPaymentInformation(paymentTableView, email); // Refresh table after payment confirmation
                            paymentStage.close();
                            cardDetailsStage.close();
                        });
        
                        Scene cardDetailsScene = new Scene(root, 400, 300);
                        cardDetailsStage.setScene(cardDetailsScene);
                        cardDetailsStage.show();
                    }
                });
        
                VBox paymentLayout = new VBox(15);
                paymentLayout.setAlignment(Pos.CENTER);
                paymentLayout.getChildren().addAll(paymentTableView, completePaymentButton);
                paymentLayout.setStyle(miniBack);
        
                Scene paymentScene = new Scene(paymentLayout, 700, 500);
                paymentStage.setScene(paymentScene);
                paymentStage.show();
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
