package com.example.kursovaya;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleCreateOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/CreateOrder.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Створення замовлення");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleviewDrivers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/DriversInfo.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Інформація про водіїв");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleviewOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/OrdersInfo.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Інформація про замовлення");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/ClientsInfo.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Інформація про клієнтів");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewSchedules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/ScheduleInfo.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Інформація про графіки");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
