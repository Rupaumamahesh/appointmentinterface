// In: admin/view/ManagePatientsView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import com.medibook.hospital.appointmentinterface.dao.PatientDAO;
import com.medibook.hospital.appointmentinterface.model.Patient;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.Optional;

public class ManagePatientsView {

    private TableView<Patient> table;
    private final PatientDAO patientDAO;

    public ManagePatientsView() {
        this.patientDAO = new PatientDAO();
    }

    public Node getView() {
        BorderPane layout = new BorderPane();

        // --- Header ---
        HBox header = new HBox();
        Label title = new Label("Manage Patients");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button registerBtn = new Button("Register New Patient");
        registerBtn.getStyleClass().add("action-button");
        registerBtn.setOnAction(e -> showRegisterPatientDialog()); // Wire up the button
        header.getChildren().addAll(title, spacer, registerBtn);
        layout.setTop(header);

        // --- Table ---
        table = createPatientTable();
        loadAllPatients(); // Load data dynamically
        layout.setCenter(table);

        BorderPane.setMargin(table, new Insets(20, 0, 0, 0));

        return layout;
    }

    private void loadAllPatients() {
        table.setItems(FXCollections.observableArrayList(patientDAO.getAllPatients()));
    }
    // In: admin/view/ManagePatientsView.java

    private void showRegisterPatientDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register New Patient");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        DatePicker dobPicker = new DatePicker();
        ComboBox<String> genderComboBox = new ComboBox<>(FXCollections.observableArrayList("Male", "Female", "Other"));
        TextField phoneField = new TextField();     // NEW
        phoneField.setPromptText("Phone Number");
        TextField addressField = new TextField();   // NEW
        addressField.setPromptText("Address");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (Email)");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Date of Birth:"), 0, 1);
        grid.add(dobPicker, 1, 1);
        grid.add(new Label("Gender:"), 0, 2);
        grid.add(genderComboBox, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);          // NEW
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);        // NEW
        grid.add(addressField, 1, 4);
        grid.add(new Label("Username:"), 0, 5);
        grid.add(usernameField, 1, 5);
        grid.add(new Label("Password:"), 0, 6);
        grid.add(passwordField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == registerButtonType) {
            if (fullNameField.getText().isEmpty() || dobPicker.getValue() == null || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Full Name, DOB, Username, and Password are required.");
                return;
            }

            // --- THIS IS THE FIX ---
            // Call the DAO method with all 7 required arguments
            int newUserId = patientDAO.registerPatientWithCredentials(
                    fullNameField.getText(),
                    dobPicker.getValue(),
                    genderComboBox.getValue(),
                    phoneField.getText(),       // NEW
                    addressField.getText(),     // NEW
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (newUserId != -1) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient registered successfully.");
                loadAllPatients(); // Refresh the table
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Could not register patient. The username might already exist.");
            }
        }
    }

    private TableView<Patient> createPatientTable() {
        TableView<Patient> tv = new TableView<>();
        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        TableColumn<Patient, LocalDate> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());
        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());

        tv.getColumns().addAll(nameCol, dobCol, genderCol);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tv;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}