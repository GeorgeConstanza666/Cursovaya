package com.example.kursovaya;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class OrderConfirmationController {

    @FXML
    private TextField pickupLocationField;

    @FXML
    private TextField dropoffLocationField;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField numberOfPassengersField;

    @FXML
    private Label receiptLabel;

    @FXML
    private void calculateOrder() {
        String pickupLocation = pickupLocationField.getText();
        String dropoffLocation = dropoffLocationField.getText();
        double distance;
        int numberOfPassengers;

        try {
            distance = Double.parseDouble(distanceField.getText());
            numberOfPassengers = Integer.parseInt(numberOfPassengersField.getText());
        } catch (NumberFormatException e) {
            showAlert("Введено некоректні дані. Введіть числа для відстані та кількості пасажирів.");
            return;
        }

        boolean result = Register.getInstance().isOrderPossible(pickupLocation, dropoffLocation, distance, numberOfPassengers);

        if (result) {
            System.out.println("Замовлення можливе та було додане до бази даних.");
            double estimatedFare = Register.getInstance().calculateFare(distance);
            String orderTime = Register.getInstance().calculateOrderTime(distance);
            Receipt receipt = new Receipt(pickupLocation, dropoffLocation, distance, numberOfPassengers, orderTime, estimatedFare);
            displayReceipt(receipt);
        } else {
            System.out.println("Замовлення не можливе. Не знайдено доступного водія.");
        }
    }

    private void displayReceipt(Receipt receipt) {
        String receiptText = String.format(
                "Pickup Location: %s\nDropoff Location: %s\nDistance: %.2f km\nNumber of Passengers: %d\nOrder Time: %s\nEstimated Fare: $%.2f",
                receipt.getPickupLocation(),
                receipt.getDropoffLocation(),
                receipt.getDistance(),
                receipt.getNumberOfPassengers(),
                receipt.getOrderTime(),
                receipt.getEstimatedFare()
        );
        receiptLabel.setText(receiptText);
    }

    @FXML
    private void confirmOrder() {
        Register.getInstance().confirmOrder();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void validateDistanceInput(KeyEvent event) {
        String input = distanceField.getText();
        if (!input.matches("\\d*\\.?\\d*")) {
            showAlert("Введено некоректні дані. Введіть лише числа для відстані.");
            distanceField.setText(input.replaceAll("[^\\d\\.]", ""));
            event.consume();
        }
    }

    @FXML
    private void validatePassengerInput(KeyEvent event) {
        String input = numberOfPassengersField.getText();
        if (!input.matches("\\d*")) {
            showAlert("Введено некоректні дані. Введіть лише цілі числа для кількості пасажирів.");
            numberOfPassengersField.setText(input.replaceAll("[^\\d]", ""));
            event.consume();
        }
    }

}
