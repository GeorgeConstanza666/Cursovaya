package com.example.kursovaya;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrdersInfoController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> orderIDColumn;
    @FXML
    private TableColumn<Order, String> pickupLocationColumn;
    @FXML
    private TableColumn<Order, String> dropoffLocationColumn;
    @FXML
    private TableColumn<Order, Double> distanceColumn;
    @FXML
    private TableColumn<Order, Integer> numberOfPassengersColumn;
    @FXML
    private TableColumn<Order, Integer> driverIDColumn;
    @FXML
    private TableColumn<Order, Integer> clientIDColumn;
    @FXML
    private TextField orderIDField;

    @FXML
    private void initialize() {
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        pickupLocationColumn.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        dropoffLocationColumn.setCellValueFactory(new PropertyValueFactory<>("dropoffLocation"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        numberOfPassengersColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPassengers"));
        driverIDColumn.setCellValueFactory(new PropertyValueFactory<>("driverID"));
        clientIDColumn.setCellValueFactory(new PropertyValueFactory<>("clientID"));

        loadOrdersFromDatabase();
    }

    private void loadOrdersFromDatabase() {
        ObservableList<Order> ordersList = FXCollections.observableArrayList();

        String query = "SELECT * FROM Orders";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int orderID = resultSet.getInt("OrderID");
                String pickupLocation = resultSet.getString("PickupLocation");
                String dropoffLocation = resultSet.getString("DropoffLocation");
                double distance = resultSet.getDouble("Distance");
                int numberOfPassengers = resultSet.getInt("NumberOfPassengers");
                int driverID = resultSet.getInt("DriverID");
                int clientID = resultSet.getInt("ClientID");

                Order order = new Order(orderID, pickupLocation, dropoffLocation, distance, numberOfPassengers, driverID, clientID);
                ordersList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ordersTable.setItems(ordersList);
    }

    @FXML
    private void deleteOrder() {
        String orderIdText = orderIDField.getText();
        if (orderIdText != null && !orderIdText.isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdText);
                Register.getInstance().deleteOrder(orderId);
                loadOrdersFromDatabase(); // Refresh the table after deletion
            } catch (NumberFormatException e) {
                System.out.println("Invalid OrderID: " + orderIdText);
            }
        }
    }
}
