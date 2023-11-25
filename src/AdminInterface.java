import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminInterface {

    public static Scene createScene(Stage primaryStage) {
        Text welcomeText = new Text("Welcome, Admin!");
        Button updateInventoryButton = new Button("Update Inventory");
        Button makeReservationButton = new Button("Make Reservation");

        VBox adminLayout = new VBox(20);
        adminLayout.setAlignment(Pos.CENTER);
        adminLayout.getChildren().addAll(welcomeText, updateInventoryButton, makeReservationButton);

        Scene adminScene = new Scene(adminLayout, 700, 500);
        adminScene.getRoot().setStyle("-fx-background-color: lightyellow;");

        updateInventoryButton.setOnAction(e -> {
            // Add action for Update Inventory button
        });

        makeReservationButton.setOnAction(e -> {
            // Add action for Make Reservation button
        });

        return adminScene;
    }
}
