import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomerInterface {

    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Customer!");
        Button makeReservationButton = new Button("Make Reservation");
        Button placeOrderButton = new Button("Place Order");
        Button cancelOrderButton = new Button("Cancel Order");
        Button makePaymentButton = new Button("Make Payment");

        VBox customerLayout = new VBox(20);
        customerLayout.setAlignment(Pos.CENTER);
        customerLayout.getChildren().addAll(welcomeText, makeReservationButton, placeOrderButton, makePaymentButton);

        Scene customerScene = new Scene(customerLayout, 900, 750);
        customerScene.getRoot().setStyle("-fx-background-color: lightgreen;");

        makeReservationButton.setOnAction(e -> {
            // Add action for Make Reservation button

            

        });

        placeOrderButton.setOnAction(e -> {
            // Add action for Place Order button
        });

        makePaymentButton.setOnAction(e -> {
            // Add action for Make Payment button
        });

        return customerScene;
    }
}
