import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDate;

public class Reservation {
    public String customerName;
    public String email;
    public Time reservationTime;
    public Date reservationDate;
    public int numPeople;

    public Reservation(String customerName, String email, Time reservationTime, Date reservationDate, int numPeople) {
        this.customerName = customerName;
        this.email = email;
        this.reservationTime = reservationTime;
        this.reservationDate = reservationDate;
        this.numPeople = numPeople;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public static void makeReservation(String customerName, String email, Time reservationTime, Date reservationDate, int numPeople) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservation (cust_name, email, reservation_time, reservation_date, numPeople) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, email);
            preparedStatement.setTime(3, reservationTime);
            preparedStatement.setDate(4, reservationDate);
            preparedStatement.setInt(5, numPeople);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Reservation exists!");
            e.printStackTrace();
        }
    }

    public static void fetchAndDisplayReservations(TableView<Reservation> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation");

            ObservableList<Reservation> reservations = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String custName = resultSet.getString("cust_name");
                String email = resultSet.getString("email");
                Time reservationTime = resultSet.getTime("reservation_time");
                Date reservationDate = resultSet.getDate("reservation_date");
                int numPeople = resultSet.getInt("numPeople");

                Reservation reservation = new Reservation(custName, email, reservationTime, reservationDate, numPeople);
                reservations.add(reservation);
            }

            tableView.setItems(reservations);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Getting Issue Fetching the Reservation Data!");
            e.printStackTrace();
        }
    }

    public static void cancelReservation(String email) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Unable to Cancel Reservation. Please recheck your Reservation Email Address!");
            e.printStackTrace();
        }
    }

    public static void showReservationMadePopup() {
        Stage confirmationStage = new Stage();
        confirmationStage.setTitle("Reservation Confirmation");

        Label confirmationLabel = new Label("Reservation successfully made!");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> confirmationStage.close());
        
        VBox confirmationLayout = new VBox(10);
        confirmationLayout.setAlignment(Pos.CENTER);
        confirmationLayout.getChildren().addAll(confirmationLabel, closeButton);

        Scene confirmationScene = new Scene(confirmationLayout, 300, 150);
        confirmationStage.setScene(confirmationScene);
        confirmationStage.showAndWait();
    }

    public static void showReservationCancelledPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Reservation Cancelled");

        Text popupText = new Text("Reservation has been cancelled successfully!");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupStage.close());

        VBox popupLayout = new VBox(20);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(popupText, closeButton);

        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}