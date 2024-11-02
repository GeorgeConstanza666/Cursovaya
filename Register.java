package com.example.kursovaya;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register {
    private static Register instance;

    private Register() {}

    public static Register getInstance() {
        if (instance == null) {
            instance = new Register();
        }
        return instance;
    }

    public void deleteDriver(int driverID) {
        String deleteShiftsQuery = "DELETE FROM ShiftSchedules WHERE DriverID = ?";
        String deleteDriverQuery = "DELETE FROM Drivers WHERE DriverID = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Почати транзакцію

            try (PreparedStatement deleteShiftsStmt = connection.prepareStatement(deleteShiftsQuery);
                 PreparedStatement deleteDriverStmt = connection.prepareStatement(deleteDriverQuery)) {

                // Видалити записи з shiftschedules
                deleteShiftsStmt.setInt(1, driverID);
                deleteShiftsStmt.executeUpdate();

                // Видалити записи з drivers
                deleteDriverStmt.setInt(1, driverID);
                deleteDriverStmt.executeUpdate();

                connection.commit(); // Застосувати транзакцію

                System.out.println("Driver with ID " + driverID + " deleted successfully.");
            } catch (SQLException e) {
                connection.rollback(); // Відкат транзакції в разі помилки
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public void deleteClient(int clientID) {
        String deleteClientQuery = "DELETE FROM Clients WHERE ClientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteClientStmt = connection.prepareStatement(deleteClientQuery)) {

            deleteClientStmt.setInt(1, clientID);
            deleteClientStmt.executeUpdate();

            System.out.println("Client with ID " + clientID + " deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int orderID) {
        String deleteOrderQuery = "DELETE FROM Orders WHERE OrderID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery)) {

            deleteOrderStmt.setInt(1, orderID);
            deleteOrderStmt.executeUpdate();

            System.out.println("Order with ID " + orderID + " deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calculateFare(double distance) {
        String fareRateQuery = "SELECT BaseRate, PerKmRate FROM farerates WHERE FareRateID = 1";
        double baseRate = 0;
        double perKmRate = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement fareRateStatement = connection.prepareStatement(fareRateQuery);
             ResultSet fareRateResultSet = fareRateStatement.executeQuery()) {

            if (fareRateResultSet.next()) {
                baseRate = fareRateResultSet.getDouble("BaseRate");
                perKmRate = fareRateResultSet.getDouble("PerKmRate");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return baseRate + (perKmRate * distance);
    }

    public boolean isOrderPossible(String pickupLocation, String dropoffLocation, double distance, int numberOfPassengers) {
        String fareRateQuery = "SELECT BaseRate, PerKmRate FROM farerates WHERE FareRateID = 1";
        String insertOrderQuery = "INSERT INTO Orders (ClientID, DriverID, PickupLocation, DropoffLocation, Distance, NumberOfPassengers, Status, OrderTime, EstimatedFare) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String selectAvailableDriverQuery = "SELECT DriverID FROM Drivers WHERE DriverID NOT IN (SELECT DriverID FROM Orders WHERE Status = 'in progress') LIMIT 1";
        String selectLatestClientQuery = "SELECT ClientID FROM Clients WHERE ClientID NOT IN (SELECT ClientID FROM Orders) ORDER BY ClientID DESC LIMIT 1";

        int latestClientID = -1;
        int availableDriverID = -1;
        double baseRate = 0;
        double perKmRate = 0;
        double estimatedFare;
        String status = "in progress";
        String orderTime = calculateOrderTime(distance);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement fareRateStatement = connection.prepareStatement(fareRateQuery);
             ResultSet fareRateResultSet = fareRateStatement.executeQuery()) {

            if (fareRateResultSet.next()) {
                baseRate = fareRateResultSet.getDouble("BaseRate");
                perKmRate = fareRateResultSet.getDouble("PerKmRate");
            }

            estimatedFare = baseRate + (perKmRate * distance);

            try (PreparedStatement selectClientStatement = connection.prepareStatement(selectLatestClientQuery);
                 ResultSet clientResultSet = selectClientStatement.executeQuery()) {

                if (clientResultSet.next()) {
                    latestClientID = clientResultSet.getInt("ClientID");
                }

                if (latestClientID == -1) {
                    System.out.println("No available client found.");
                    return false;
                }

                try (PreparedStatement selectDriverStatement = connection.prepareStatement(selectAvailableDriverQuery);
                     ResultSet driverResultSet = selectDriverStatement.executeQuery()) {

                    if (driverResultSet.next()) {
                        availableDriverID = driverResultSet.getInt("DriverID");
                    }

                    if (availableDriverID == -1) {
                        System.out.println("No available driver found.");
                        return false;
                    }

                    try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery)) {
                        insertOrderStatement.setInt(1, latestClientID);
                        insertOrderStatement.setInt(2, availableDriverID);
                        insertOrderStatement.setString(3, pickupLocation);
                        insertOrderStatement.setString(4, dropoffLocation);
                        insertOrderStatement.setDouble(5, distance);
                        insertOrderStatement.setInt(6, numberOfPassengers);
                        insertOrderStatement.setString(7, status);
                        insertOrderStatement.setString(8, orderTime);
                        insertOrderStatement.setDouble(9, estimatedFare);

                        insertOrderStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    String calculateOrderTime(double distance) {
        // Simple formula for order time calculation (e.g., 1 hour per 50 km)
        double timeInHours = distance / 50.0;
        int hours = (int) timeInHours;
        int minutes = (int) ((timeInHours % 1) * 60);
        return String.format("2024-06-04 %02d:%02d:00", hours, minutes);
    }

    public void confirmOrder() {
        String updateOrderStatusQuery = "UPDATE Orders SET Status = 'completed' WHERE Status = 'in progress' ORDER BY OrderID DESC LIMIT 1";
        String selectLastConfirmedOrderQuery = "SELECT OrderID, DriverID FROM Orders WHERE Status = 'completed' ORDER BY OrderID DESC LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement updateOrderStatusStmt = connection.prepareStatement(updateOrderStatusQuery);
             PreparedStatement selectLastConfirmedOrderStmt = connection.prepareStatement(selectLastConfirmedOrderQuery)) {

            int rowsUpdated = updateOrderStatusStmt.executeUpdate();

            if (rowsUpdated > 0) {
                try (ResultSet resultSet = selectLastConfirmedOrderStmt.executeQuery()) {
                    if (resultSet.next()) {
                        int orderId = resultSet.getInt("OrderID");
                        int driverId = resultSet.getInt("DriverID");
                        showConfirmationAlert(orderId, driverId);
                    }
                }
                System.out.println("Order has been confirmed and status updated to 'completed'.");
            } else {
                System.out.println("No order found to confirm.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationAlert(int orderId, int driverId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Замовлення з id " + orderId + " було підтверджене!\n" +
                "Водій з id " + driverId + " успішно виконав Замовлення!\n" +
                "Клієнт успішно зробив оплату замовлення!");
        alert.showAndWait();
    }
}
