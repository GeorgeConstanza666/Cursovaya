package com.example.kursovaya;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DriversInfoController {

    @FXML
    private TextField driverIDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField licenseNumberField;
    @FXML
    private TextField phoneNumberField;

    @FXML
    private TableView<Driver> driversTable;
    @FXML
    private TableColumn<Driver, Integer> driverIDColumn;
    @FXML
    private TableColumn<Driver, String> nameColumn;
    @FXML
    private TableColumn<Driver, String> licenseNumberColumn;
    @FXML
    private TableColumn<Driver, String> phoneNumberColumn;

    @FXML
    private Button addDriverButton;

    @FXML
    private TextField makeField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField capacityField;
    @FXML
    private ComboBox<Integer> driverIdComboBox;

    @FXML
    private void initialize() {
        driverIDColumn.setCellValueFactory(new PropertyValueFactory<>("driverID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        licenseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        loadDriversFromDatabase();

        addDriverButton.setOnAction(e -> addDriver());

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DriverID FROM Drivers");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                driverIdComboBox.getItems().add(rs.getInt("DriverID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDriversFromDatabase() {
        ObservableList<Driver> driversList = FXCollections.observableArrayList();

        String query = "SELECT * FROM Drivers";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int driverID = resultSet.getInt("DriverID");
                String name = resultSet.getString("Name");
                String licenseNumber = resultSet.getString("LicenseNumber");
                String phoneNumber = resultSet.getString("PhoneNumber");

                Driver driver = new Driver(driverID, name, licenseNumber, phoneNumber);
                driversList.add(driver);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        driversTable.setItems(driversList);
    }

    @FXML
    private void deleteDriver() {
        String driverIdText = driverIDField.getText();
        if (driverIdText != null && !driverIdText.isEmpty()) {
            try {
                int driverID = Integer.parseInt(driverIdText);
                Register.getInstance().deleteDriver(driverID);
                loadDriversFromDatabase(); // Оновлення таблиці після видалення
            } catch (NumberFormatException e) {
                System.out.println("Invalid DriverID: " + driverIdText);
            }
        }
    }

    @FXML
    private void addDriver() {
        String name = nameField.getText();
        String licenseNumber = licenseNumberField.getText();
        String phoneNumber = phoneNumberField.getText();

        String driverInsertQuery = "INSERT INTO Drivers (Name, LicenseNumber, PhoneNumber, EmploymentDate, Status) VALUES (?, ?, ?, ?, ?)";
        String vehicleInsertQuery = "INSERT INTO Vehicles (Make, Model, Year, Capacity, Status) VALUES ('Toyota', 'Camry', 2021, 4, 'available')";
        String shiftInsertQuery = "INSERT INTO ShiftSchedules (DriverID, VehicleID, StartTime, EndTime, Status) VALUES (?, ?, '2024-06-04 10:00:00', '2024-06-04 18:00:00', 'in progress')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement driverStatement = connection.prepareStatement(driverInsertQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement vehicleStatement = connection.prepareStatement(vehicleInsertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Вставка водія
            driverStatement.setString(1, name);
            driverStatement.setString(2, licenseNumber);
            driverStatement.setString(3, phoneNumber);
            driverStatement.setDate(4, java.sql.Date.valueOf("2024-06-04"));
            driverStatement.setString(5, "active");
            driverStatement.executeUpdate();

            // Отримання ID нового водія
            ResultSet driverKeys = driverStatement.getGeneratedKeys();
            driverKeys.next();
            int newDriverID = driverKeys.getInt(1);

            // Вставка транспортного засобу
            vehicleStatement.executeUpdate();

            // Отримання ID нового транспортного засобу
            ResultSet vehicleKeys = vehicleStatement.getGeneratedKeys();
            vehicleKeys.next();
            int newVehicleID = vehicleKeys.getInt(1);

            // Вставка графіка змін
            try (PreparedStatement shiftStatement = connection.prepareStatement(shiftInsertQuery)) {
                shiftStatement.setInt(1, newDriverID);
                shiftStatement.setInt(2, newVehicleID);
                shiftStatement.executeUpdate();
            }

            // Оновлення таблиці водіїв
            loadDriversFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTransport() {
        String make = makeField.getText();
        String model = modelField.getText();
        int year = Integer.parseInt(yearField.getText());
        int capacity = Integer.parseInt(capacityField.getText());
        int driverId = driverIdComboBox.getValue();

        String insertTransportQuery = "INSERT INTO Vehicles (Make, Model, Year, Capacity, Status) VALUES (?, ?, ?, ?, 'active')";
        String selectOldVehicleQuery = "SELECT VehicleID FROM ShiftSchedules WHERE DriverID = ?";
        String updateShiftScheduleQuery = "UPDATE ShiftSchedules SET VehicleID = ? WHERE DriverID = ?";
        String deleteOldVehicleQuery = "DELETE FROM Vehicles WHERE VehicleID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertTransportQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement selectOldVehicleStmt = connection.prepareStatement(selectOldVehicleQuery);
             PreparedStatement updateShiftScheduleStmt = connection.prepareStatement(updateShiftScheduleQuery);
             PreparedStatement deleteOldVehicleStmt = connection.prepareStatement(deleteOldVehicleQuery)) {

            // Вставка нового транспортного засобу
            insertStmt.setString(1, make);
            insertStmt.setString(2, model);
            insertStmt.setInt(3, year);
            insertStmt.setInt(4, capacity);

            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating vehicle failed, no rows affected.");
            }

            int newVehicleId;
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newVehicleId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }

            // Отримання старого транспортного засобу
            selectOldVehicleStmt.setInt(1, driverId);
            int oldVehicleId = -1;
            try (ResultSet resultSet = selectOldVehicleStmt.executeQuery()) {
                if (resultSet.next()) {
                    oldVehicleId = resultSet.getInt("VehicleID");
                }
            }

            // Оновлення графіка змін з новим транспортним засобом
            updateShiftScheduleStmt.setInt(1, newVehicleId);
            updateShiftScheduleStmt.setInt(2, driverId);
            updateShiftScheduleStmt.executeUpdate();

            // Видалення старого транспортного засобу, якщо він існує
            if (oldVehicleId != -1) {
                deleteOldVehicleStmt.setInt(1, oldVehicleId);
                deleteOldVehicleStmt.executeUpdate();
            }

            showAlert("Success", "Transport added and shift schedule updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add transport and update shift schedule.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
