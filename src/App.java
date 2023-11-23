import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
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
        Scene scene = new Scene(layout, 700, 500);
        
        scene.getRoot().setStyle("-fx-background-color: lightblue;");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Methods to open new interfaces
    private void openCustomerInterface() {
        // Logic to open Customer interface
        System.out.println("Opening Customer Interface");
        // You would create a new scene here and define the Customer interface elements
    }

    private void openAdminInterface() {
        // Logic to open Admin interface
        System.out.println("Opening Admin Interface");
        // You would create a new scene here and define the Admin interface elements
    }

    private void openChefInterface() {
        // Logic to open Chef interface
        System.out.println("Opening Chef Interface");
        // You would create a new scene here and define the Chef interface elements
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
