import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.*;

public class Item {
    public String itemName;
    public int quantity;
    public int price;
    public String description;
    private int upperLimit;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private int orderId;
    private int orderItemId;

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean isSelected) {
        this.selected.set(isSelected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public Item(int order_item_id, int order_id, String itemName, int quantity) {
        this.orderItemId = order_item_id;
        this.orderId = order_id;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.upperLimit = this.quantity;
    }

    public Item(String itemName2, int price, String description) {
        this.itemName = itemName2;
        this.price = price;
        this.description = description;
    }

    public void setOrderItemId(int order_item_id) {
        this.orderItemId = order_item_id;
    }

    public int getOrderItemId() {
        return this.orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperlimit) {
        this.upperLimit = upperlimit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    public static void fetchAndDisplayInventoryData(TableView<Item> tableView) {
        try {

            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();

            // Query to select data from the inventory table
            String query = "SELECT * FROM inventory";
            ResultSet resultSet = statement.executeQuery(query);

            // Populate the TableView with retrieved data
            ObservableList<Item> inventoryList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");
                
                // // Fetching additional details from the menu table using JOIN
                // String menuQuery = "SELECT * FROM menu WHERE item_name = '" + itemName + "'";
                // ResultSet menuResultSet = statement.executeQuery(menuQuery);
                // if (menuResultSet.next()) {
                //     int price = menuResultSet.getInt("price");
                //     String description = menuResultSet.getString("descrip");
                    inventoryList.add(new Item(itemName, quantity));
                // }
                // menuResultSet.close();
            }

            tableView.setItems(inventoryList);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Unable to fetch Inventory Data!");
            e.printStackTrace();
        }
    }

    public static void addItemToInventory(String itemName, int quantity) {
        try {
            // Your database connection details
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
    
            // Query to insert a new item into the inventory table
            String insertOrUpdateQuery = "INSERT INTO inventory (item_name, quantity) VALUES ('" + itemName + "', " + quantity +
                ") ON DUPLICATE KEY UPDATE quantity = quantity + " + quantity;
            statement.executeUpdate(insertOrUpdateQuery);
    
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to Update the Inventory!");
            ex.printStackTrace();
        }
    }
    
    public static void fetchAndDisplayMenuData(TableView<Item> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
    
            String query = "SELECT * FROM menu";
            ResultSet resultSet = statement.executeQuery(query);
    
            ObservableList<Item> menuList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("descrip");
                menuList.add(new Item(itemName, price, description)); // Add items to the menuList
            }
    
            tableView.setItems(menuList);
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to fetch Menu Data");
            ex.printStackTrace();
        }
    }
    
    public static void addToMenu(String itemName, int price, String description) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // // Check if the item already exists in the menu
            // PreparedStatement checkIfExists = connection.prepareStatement("SELECT item_name FROM menu WHERE item_name = ?");
            // checkIfExists.setString(1, itemName);
            // ResultSet existingItem = checkIfExists.executeQuery();
    
            // if (!existingItem.next()) {
                // If the item doesn't exist, proceed to insert it into the menu
                PreparedStatement insertItem = connection.prepareStatement("INSERT INTO menu (item_name, price, descrip) VALUES (?, ?, ?)");
                insertItem.setString(1, itemName);
                insertItem.setInt(2, price);
                insertItem.setString(3, description);
                insertItem.executeUpdate();
    
                insertItem.close();
            // } else {
                // System.out.println("Item already exists in the menu.");
            // }
    
            // existingItem.close();
            // checkIfExists.close();
            connection.close();
        } catch (SQLException e) {
            showAlert("Menu item already exists!");
            e.printStackTrace();
        }
    }

    public static void fetchAvailableMenuItems(TableView<Item> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM inventory JOIN menu ON inventory.item_name = menu.item_name WHERE inventory.quantity > 0";
            ResultSet resultSet = statement.executeQuery(query);

            ObservableList<Item> menuList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String itemName = resultSet.getString("menu.item_name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("descrip");
                int quantity = resultSet.getInt("quantity");

                // Create an Item object with fetched data
                Item item = new Item(itemName, quantity);
                item.setPrice(price);
                item.setDescription(description);

                menuList.add(item); // Add items to the menuList
            }

            tableView.setItems(menuList);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to fetch Menu items!");
            ex.printStackTrace();
        }
    }

    public static void processSelectedItems(ObservableList<Item> selectedItems) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // Generate a unique order ID (you might need a proper ID generator in practice)
            int orderID = generateUniqueOrderID(connection);
            int totalPrice = 0;
            // Insert into the 'orders' table
            PreparedStatement insertOrderStmt = connection.prepareStatement("INSERT INTO orders (order_id, reservation_id) VALUES (?, ?)");
            insertOrderStmt.setInt(1, orderID);
            // Assuming reservation_id is hard-coded here; in reality, it should come from a specific reservation
            if(CustomerInterface.isValidReservation(CustomerInterface.email)) {
                insertOrderStmt.setString(2, CustomerInterface.email); // Replace this with the actual reservation ID
            }
            insertOrderStmt.executeUpdate();
            insertOrderStmt.close();
    
            // Insert each selected item into the 'order_items' table
            PreparedStatement insertOrderItemsStmt = connection.prepareStatement("INSERT INTO order_items (order_id, menu_item_name, quantity) VALUES (?, ?, ?)");
            PreparedStatement insertPaymentStmt = connection.prepareStatement("INSERT INTO payment (order_id, total_price, payment_status) VALUES (?, ?, ?)");
            for (Item selectedItem : selectedItems) {
                insertOrderItemsStmt.setInt(1, orderID);
                insertOrderItemsStmt.setString(2, selectedItem.getItemName());
                // Assuming a default quantity of 1; you can change this based on user input
                PreparedStatement quantityStmt = connection.prepareStatement("SELECT quantity FROM inventory WHERE item_name = ?");
                quantityStmt.setString(1, selectedItem.getItemName());

                ResultSet resultSet = quantityStmt.executeQuery();
                int invent_Quantity = 0; // Default value if no quantity is found
                if (resultSet.next()) {
                    invent_Quantity = resultSet.getInt("quantity");
                }

                // Update the quantity in the order_items table by subtracting the selected quantity
                int updatedQuantity = invent_Quantity - selectedItem.getQuantity();

                PreparedStatement getPriceStmt = connection.prepareStatement("SELECT price FROM menu WHERE item_name = ?");
                getPriceStmt.setString(1, selectedItem.getItemName());
                ResultSet priceResultSet = getPriceStmt.executeQuery();
    
                if (priceResultSet.next()) {
                    int price = priceResultSet.getInt("price");
                    totalPrice += price * updatedQuantity;
                }
                getPriceStmt.close();
            
                if (updatedQuantity >= 0) {
                    insertOrderItemsStmt.setInt(3, updatedQuantity);
                    insertOrderItemsStmt.executeUpdate();
                    PreparedStatement updateInventoryStmt = connection.prepareStatement("UPDATE inventory SET quantity = ? WHERE item_name = ?");
                    updateInventoryStmt.setInt(1, selectedItem.getQuantity());
                    updateInventoryStmt.setString(2, selectedItem.getItemName());
                    updateInventoryStmt.executeUpdate();
                }
                
                
            }
            // Insert into the 'payment' table
            insertPaymentStmt.setInt(1, orderID);
            insertPaymentStmt.setInt(2, totalPrice);
            insertPaymentStmt.setString(3, "unpaid");
            insertPaymentStmt.executeUpdate();

            insertOrderItemsStmt.close();
            insertPaymentStmt.close();
            connection.close();
            
            // Display a success message or perform any other necessary actions here
            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            showAlert("Unable to place your order. Please order again!");
            e.printStackTrace();
        }
    }

    private static int generateUniqueOrderID(Connection connection) throws SQLException {
        int orderID = 0;
        PreparedStatement stmt = connection.prepareStatement("SELECT MAX(order_id) FROM orders");
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            orderID = resultSet.getInt(1);
            if (resultSet.wasNull()) {
                orderID = 0; // Set to default if null
            }
            orderID++; // Increment to generate the next unique ID
        }
        resultSet.close();
        stmt.close();
        return orderID;
    }
    

    public static void fetchPaymentInformation(TableView<Payment> tableView, String email) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
    
            // Prepare a query to fetch payment information based on the provided email
            String selectPaymentQuery = "SELECT * FROM payment INNER JOIN orders ON payment.order_id = orders.order_id WHERE orders.reservation_id = ? AND payment.payment_status = 'unpaid'";
    
            PreparedStatement selectPaymentStmt = connection.prepareStatement(selectPaymentQuery);
            selectPaymentStmt.setString(1, email);
    
            ResultSet paymentResultSet = selectPaymentStmt.executeQuery();
    
            ObservableList<Payment> paymentList = FXCollections.observableArrayList();
            // Check if payment details are found
            while (paymentResultSet.next()) {
                int orderID = paymentResultSet.getInt("order_id");
                int totalPrice = paymentResultSet.getInt("total_price");
                String paymentStatus = paymentResultSet.getString("payment_status");
    
                // Display or utilize payment information
                // System.out.println("Order ID: " + orderID);
                // System.out.println("Total Price: " + totalPrice);
                // System.out.println("Payment Status: " + paymentStatus);
                
                Payment payment = new Payment(orderID, totalPrice, paymentStatus);
                
                paymentList.add(payment);
            }
            tableView.setItems(paymentList);
    
            // Close resources
            paymentResultSet.close();
            selectPaymentStmt.close();
            connection.close();
    
        } catch (SQLException exception) {
            showAlert("Error fetching payment information.");
            exception.printStackTrace();
            }
    }

    public static void updatePaymentStatusToPaid(int orderID, String email) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            
            // Start a transaction to ensure atomicity
            connection.setAutoCommit(false);
            
            // Prepare the update query for the payment table
            String updatePaymentQuery = "UPDATE payment SET payment_status = 'paid' WHERE order_id = ?";
            PreparedStatement updatePaymentStmt = connection.prepareStatement(updatePaymentQuery);
            updatePaymentStmt.setInt(1, orderID);
            
            // Execute the update for payment status
            int rowsAffected = updatePaymentStmt.executeUpdate();
    
            // Flag to track successful operations
            boolean paymentUpdated = rowsAffected > 0;
            boolean ordersDeleted = false;
            boolean reservationDeleted = false;
    
            if (paymentUpdated) {
                System.out.println("Payment status updated to 'paid' for Order ID: " + orderID);
    
                // Delete entries from orders table
                String deleteOrdersQuery = "DELETE FROM orders WHERE order_id = ?";
                PreparedStatement deleteOrdersStmt = connection.prepareStatement(deleteOrdersQuery);
                deleteOrdersStmt.setInt(1, orderID);
                int rowsDeleted = deleteOrdersStmt.executeUpdate();
                ordersDeleted = rowsDeleted > 0;
                deleteOrdersStmt.close();
    
                if (ordersDeleted) {
                    System.out.println("Order ID: " + orderID + " deleted from orders table.");
    
                    // Attempt to delete the reservation
                    String deleteReservationQuery = "DELETE FROM reservation WHERE email = ?";
                    PreparedStatement deleteReservationStmt = connection.prepareStatement(deleteReservationQuery);
                    deleteReservationStmt.setString(1, email);
                    int reservationRowsDeleted = deleteReservationStmt.executeUpdate();
                    reservationDeleted = reservationRowsDeleted > 0;
                    deleteReservationStmt.close();
    
                    if (reservationDeleted) {
                        System.out.println("Reservation with email: " + email + " deleted from reservation table.");
                    } else {
                        System.out.println("Failed to delete reservation with email: " + email);
                    }
                }
            } else {
                System.out.println("No payment found for Order ID: " + orderID);
                // Handle the case where no payment was found for the provided orderID
            }
    
            // Commit or rollback the transaction based on operations
            if (paymentUpdated && ordersDeleted && reservationDeleted) {
                connection.commit();
            } else {
                connection.rollback();
            }
    
            // Close resources
            updatePaymentStmt.close();
            connection.close();
        } catch (SQLException exception) {
            // Rollback the transaction in case of any exception
            try {
                if (exception != null && connection != null) {
                    exception.printStackTrace();
                    System.out.println("Rolling back transaction...");
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            showAlert("Error updating payment status.");
            exception.printStackTrace();
        } finally {
            // Close resources in the finally block
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }
    
    
    

    
    public static void showOrderConfirmation() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();
    }
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void fetchOrderItemsData(TableView<Item> tableView) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD);
            Statement statement = connection.createStatement();
    
            String query = "SELECT * FROM order_items";
            ResultSet resultSet = statement.executeQuery(query);
    
            ObservableList<Item> orderItemsList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int orderItemId = resultSet.getInt("order_item_id");
                int orderId = resultSet.getInt("order_id");
                String menuItemName = resultSet.getString("menu_item_name");
                int quantity = resultSet.getInt("quantity");
    
                // Create an Item object with fetched data
                Item orderItem = new Item(orderItemId, orderId, menuItemName, quantity);
                orderItemsList.add(orderItem); // Add items to the orderItemsList
            }
    
            tableView.setItems(orderItemsList);
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            showAlert("Unable to fetch Order Items Data");
            ex.printStackTrace();
        }
    }
    
    
    public static void deleteOrderItem(int orderItemId) {
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL, DatabaseConnection.USERNAME, DatabaseConnection.PASSWORD)) {
            // Create the DELETE SQL statement for order_items
            String deleteOrderItemQuery = "DELETE FROM order_items WHERE order_item_id = ?";
            String getOrderIDQuery = "SELECT order_id FROM order_items WHERE order_item_id = ?";
            
            try (PreparedStatement deleteOrderItemStatement = connection.prepareStatement(deleteOrderItemQuery);
                 PreparedStatement getOrderIDStatement = connection.prepareStatement(getOrderIDQuery)) {
    
                // Set the parameter for the order_item_id to be deleted
                deleteOrderItemStatement.setInt(1, orderItemId);
                getOrderIDStatement.setInt(1, orderItemId);
    
                // Get the order_id for the specific order_item_id
                ResultSet resultSet = getOrderIDStatement.executeQuery();
                int orderID = -1;
                if (resultSet.next()) {
                    orderID = resultSet.getInt("order_id");
                }
    
                // Execute the delete statement for order_items
                int rowsAffected = deleteOrderItemStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    System.out.println("Order item deleted successfully!");
                } else {
                    System.out.println("Order item not found or could not be deleted.");
                }
    
                // Check if the order_id exists in other rows of order_items
                // if (orderID != -1) {
                //     String checkOrderIDQuery = "SELECT * FROM order_items WHERE order_id = ?";
                //     try (PreparedStatement checkOrderIDStatement = connection.prepareStatement(checkOrderIDQuery)) {
                //         checkOrderIDStatement.setInt(1, orderID);
                //         ResultSet checkResultSet = checkOrderIDStatement.executeQuery();
                        
                //         // If the order_id doesn't exist in any other row of order_items, delete it from orders table
                //         if (!checkResultSet.next()) {
                //             String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";
                //             try (PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery)) {
                //                 deleteOrderStatement.setInt(1, orderID);
                //                 deleteOrderStatement.executeUpdate();
                //                 System.out.println("Order removed from orders table.");
                //             }
                //         }
                //     }
                // }
            }
        } catch (SQLException e) {
            showAlert("Unable to Complete Order!");
            System.err.println("Error deleting order item: " + e.getMessage());
        }
    }
    

    // public static Item createOrderItem(int orderId, String menuItemName, int quantity) {
    //     Item orderItem = new Item();
    //     orderItem.setOrderId(orderId); // Set the orderId property
    //     orderItem.setItemName(menuItemName);
    //     orderItem.setQuantity(quantity);
    //     return orderItem;
    // }    
    
}
