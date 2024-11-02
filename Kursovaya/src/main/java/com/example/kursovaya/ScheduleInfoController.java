package com.example.kursovaya;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleInfoController {

    @FXML
    private TableView<Schedule> schedulesTable;
    @FXML
    private TableColumn<Schedule, Integer> ShiftIDColumn;
    @FXML
    private TableColumn<Schedule, Integer> driverIDColumn;
    @FXML
    private TableColumn<Schedule, Integer> vehicleIDColumn;
    @FXML
    private TableColumn<Schedule, String> startTimeColumn;
    @FXML
    private TableColumn<Schedule, String> endTimeColumn;
    @FXML
    private TableColumn<Schedule, String> statusColumn;

    @FXML
    private TextField ShiftIDField;

    @FXML
    private Button button;

    @FXML
    private void initialize() {
        ShiftIDColumn.setCellValueFactory(new PropertyValueFactory<>("ShiftID"));
        driverIDColumn.setCellValueFactory(new PropertyValueFactory<>("driverID"));
        vehicleIDColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadSchedulesFromDatabase();

        button.setOnAction(e -> deleteSchedule());
    }

    private void loadSchedulesFromDatabase() {
        ObservableList<Schedule> schedulesList = FXCollections.observableArrayList();

        String query = "SELECT * FROM shiftschedules";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int ShiftID = resultSet.getInt("ShiftID");
                int driverID = resultSet.getInt("DriverID");
                int vehicleID = resultSet.getInt("VehicleID");
                String startTime = resultSet.getString("StartTime");
                String endTime = resultSet.getString("EndTime");
                String status = resultSet.getString("Status");

                Schedule schedule = new Schedule(ShiftID, driverID, vehicleID, startTime, endTime, status);
                schedulesList.add(schedule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        schedulesTable.setItems(schedulesList);
    }

    @FXML
    private void deleteSchedule() {
        String ShiftIDText = ShiftIDField.getText();
        if (ShiftIDText != null && !ShiftIDText.isEmpty()) {
            try {
                int ShiftID = Integer.parseInt(ShiftIDText);
                String deleteQuery = "DELETE FROM shiftschedules WHERE ShiftID = ?";
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

                    statement.setInt(1, ShiftID);
                    int affectedRows = statement.executeUpdate();

                    if (affectedRows > 0) {
                        showAlert("Success", "Schedule deleted successfully!");
                        loadSchedulesFromDatabase();
                    } else {
                        showAlert("Error", "ShiftID ID not found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to delete schedule.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Shift ID.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
