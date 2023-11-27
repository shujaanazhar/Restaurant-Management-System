import java.sql.*;
import java.time.LocalDate;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;



public class CustomerInterface {

    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Customer!");
        Button makeReservationButton = new Button("Reserve Table");
        Button placeOrderButton = new Button("Place Order");
        Button cancelReservationButton = new Button("Cancel Reservation");
        Button makePaymentButton = new Button("Make Payment");

        welcomeText.setStyle("-fx-font-size: 16pt;");
        makeReservationButton.setStyle("-fx-font-size: 14pt;");
        cancelReservationButton.setStyle("-fx-font-size: 14pt;");
        placeOrderButton.setStyle("-fx-font-size: 14pt;");
        makePaymentButton.setStyle("-fx-font-size: 14pt;");

        VBox customerLayout = new VBox(20);
        customerLayout.setAlignment(Pos.CENTER);
        customerLayout.getChildren().addAll(welcomeText, makeReservationButton, cancelReservationButton, placeOrderButton, makePaymentButton);

        Scene customerScene = new Scene(customerLayout, 900, 750);
        customerScene.getRoot().setStyle("-fx-background-color: lightgreen;");

        makeReservationButton.setOnAction(e -> {
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
            addReservationLayout.getChildren().addAll(custNameField, emailField, datePicker, timeBox, numPeopleField);

            Scene addReservationScene = new Scene(addReservationLayout, 400, 400);
            addReservationStage.setScene(addReservationScene);
            addReservationStage.show();

            // Button to confirm reservation addition
            Button confirmReservationButton = new Button("Confirm Reservation");
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

                // Show confirmation pop-up
                Reservation.showReservationMadePopup();
            });

            addReservationLayout.getChildren().add(confirmReservationButton);
        });


        cancelReservationButton.setOnAction(e -> {
            Stage cancelReservationStage = new Stage();
            cancelReservationStage.setTitle("Cancel Reservation");

            TextField emailField = new TextField();
            emailField.setPromptText("Enter Email");

            Button confirmCancelReservationButton = new Button("Confirm Cancel");
            confirmCancelReservationButton.setOnAction(event -> {
                String email = emailField.getText();
                Reservation.cancelReservation(email);
                Reservation.showReservationCancelledPopup();
                cancelReservationStage.close();
            });

            VBox cancelReservationLayout = new VBox(10);
            cancelReservationLayout.setAlignment(Pos.CENTER);
            cancelReservationLayout.getChildren().addAll(emailField, confirmCancelReservationButton);

            Scene cancelReservationScene = new Scene(cancelReservationLayout, 300, 200);
            cancelReservationStage.setScene(cancelReservationScene);
            cancelReservationStage.show();
        });

        placeOrderButton.setOnAction(e -> {

        });

        makePaymentButton.setOnAction(e -> {

        });

        return customerScene;
    }
}
