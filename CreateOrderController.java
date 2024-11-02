package com.example.kursovaya;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateOrderController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;

    @FXML
    private void addClient() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        // Перевірка коректності даних
        if (!isValidName(name)) {
            showAlert("Некоректне ім'я клієнта. Допускаються тільки літери без цифр.");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert("Некоректний телефонний номер. Тільки цифри та знаки, не менше та не більше 10 символів.");
            return;
        }

        // Додавання клієнта у базу даних
        String query = "INSERT INTO Clients (Name, PhoneNumber, Email) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int newClientId = rs.getInt(1);
                System.out.println("Новий клієнт доданий з ID: " + newClientId);
            }

            // Закриваємо поточне вікно
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            // Відкриваємо нове вікно "Підтвердження замовлення"
            Stage confirmOrderStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/ConfirmOrder.fxml"));
            Scene scene = new Scene(loader.load());
            confirmOrderStage.setScene(scene);
            confirmOrderStage.setTitle("Підтвердження замовлення");
            confirmOrderStage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Перевірка на коректність імені клієнта
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Zа-яА-ЯіІїЇґҐёЁ]+");
    }

    // Перевірка на коректність телефонного номера
    private boolean isValidPhone(String phone) {
        return phone.matches("[0-9]+") && phone.length() >= 10 && phone.length() <= 15;
    }

    // Показує повідомлення про помилку
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
