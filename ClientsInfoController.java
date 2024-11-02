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

public class ClientsInfoController {

    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, Integer> clientIDColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TableColumn<Client, String> phoneNumberColumn;
    @FXML
    private TableColumn<Client, String> emailColumn;
    @FXML
    private TextField clientIDField;

    @FXML
    private void initialize() {
        clientIDColumn.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClientsFromDatabase();
    }

    private void loadClientsFromDatabase() {
        ObservableList<Client> clientsList = FXCollections.observableArrayList();

        String query = "SELECT * FROM Clients";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int clientID = resultSet.getInt("ClientID");
                String name = resultSet.getString("Name");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");

                Client client = new Client(clientID, name, phoneNumber, email);
                clientsList.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        clientsTable.setItems(clientsList);
    }

    @FXML
    private void deleteClient() {
        String clientIdText = clientIDField.getText();
        if (clientIdText != null && !clientIdText.isEmpty()) {
            try {
                int clientId = Integer.parseInt(clientIdText);
                Register.getInstance().deleteClient(clientId);
                loadClientsFromDatabase(); // Refresh the table after deletion
            } catch (NumberFormatException e) {
                System.out.println("Invalid ClientID: " + clientIdText);
            }
        }
    }
}
