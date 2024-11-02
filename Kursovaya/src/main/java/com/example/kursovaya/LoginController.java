package com.example.kursovaya;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            // Закриття вікна авторизації
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            // Відкриття основного вікна
            openMainAppWindow();
        } else {
            showAlert("Неправильний логін або пароль. Спробуйте ще раз.");
        }
    }

    private boolean authenticate(String username, String password) {
        return "taxi2005".equals(username) && "12345".equals(password);
    }

    private void openMainAppWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kursovaya/Main.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Організація таксі");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка авторизації");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
