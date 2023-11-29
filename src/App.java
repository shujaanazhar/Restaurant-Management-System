import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Restaurant Management System");

        Text userTypeText = new Text("Choose the user type");
        userTypeText.setStyle("-fx-font-size: 16pt;");
        // Creating buttons
        Button customerButton = new Button("Customer");
        Button adminButton = new Button("Admin");
        Button chefButton = new Button("Chef");

        // Beautify the buttons
        customerButton.setStyle("-fx-font-size: 14pt;");
        adminButton.setStyle("-fx-font-size: 14pt;");
        chefButton.setStyle("-fx-font-size: 14pt;");

        // Setting actions for buttons
        customerButton.setOnAction(e -> openCustomerInterface());
        adminButton.setOnAction(e -> openAdminInterface());
        chefButton.setOnAction(e -> openChefInterface());

        // Adding buttons to layout
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(userTypeText, customerButton, adminButton, chefButton);

        // Creating scene and setting the layout
        Scene scene = new Scene(layout, 900, 750);
        scene.getRoot().setStyle("-fx-background-color: lightblue;");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to open Customer interface
    private void openCustomerInterface() {
        Scene customerScene = CustomerInterface.createScene(primaryStage);
        primaryStage.setScene(customerScene);
    }

    // Methods for Admin and Chef interfaces
    private void openAdminInterface() {
        Scene adminScene = AdminInterface.createScene(primaryStage);
        primaryStage.setScene(adminScene);
    }

    private void openChefInterface() {
        Scene chefScene = ChefInterface.createScene(primaryStage);
        primaryStage.setScene(chefScene);
    }   

    public static void main(String[] args) {
        launch(args);
    }
}




/* RUN THIS TO CREATE DB */

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;

// public class App {
//     public static void main(String[] args) {
//         Connection conn = null;
//         Statement stmt = null;

//         try {
//             // MySQL database URL
//             String url = "jdbc:mysql://localhost:3306/";
//             String username = "root";
//             String password = "shujaanazhar";

//             // Establish a connection to the MySQL server
//             conn = DriverManager.getConnection(url, username, password);

//             if (conn != null) {
//                 System.out.println("Connected to MySQL server.");

//                 // Create a new database
//                 stmt = conn.createStatement();
//                 String dbName = "RMS_DMS"; // Replace with your database name

//                 System.out.println("Database created successfully: " + dbName);
//             }
//         } catch (SQLException e) {
//             System.out.println(e.getMessage());
//         } finally {
//             try {
//                 if (stmt != null) {
//                     stmt.close();
//                 }
//                 if (conn != null) {
//                     conn.close();
//                 }
//             } catch (SQLException ex) {
//                 System.out.println(ex.getMessage());
//             }
//         }
//     }
// }
