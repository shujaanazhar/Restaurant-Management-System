import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import java.sql.Date;
import java.sql.Time;

import java.sql.*;

public class Reservation {
    public int reservationId;
    public String customerName;
    public String email;
    public Time reservationTime;
    public Date reservationDate;
    public int numPeople;

    public Reservation(int reservationId, String customerName, String email, Time reservationTime, Date reservationDate, int numPeople) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.email = email;
        this.reservationTime = reservationTime;
        this.reservationDate = reservationDate;
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
                int reservationId = resultSet.getInt("reservation_id");
                String custName = resultSet.getString("cust_name");
                String email = resultSet.getString("email");
                Time reservationTime = resultSet.getTime("reservation_time");
                Date reservationDate = resultSet.getDate("reservation_date");
                int numPeople = resultSet.getInt("numPeople");

                Reservation reservation = new Reservation(reservationId, custName, email, reservationTime, reservationDate, numPeople);
                reservations.add(reservation);
            }

            tableView.setItems(reservations);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters for reservation attributes
}
