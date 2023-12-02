import java.sql.*;

public class ReservationManager {
    public String customerName;
    public String email;
    public Time reservationTime;
    public Date reservationDate;
    public int numPeople;

    public ReservationManager(String customerName, String email, Time reservationTime, Date reservationDate, int numPeople) {
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
}
