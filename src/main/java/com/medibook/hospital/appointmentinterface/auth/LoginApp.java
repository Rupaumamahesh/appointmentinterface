package com.medibook.hospital.appointmentinterface.auth;

import com.medibook.hospital.appointmentinterface.admin.AdminDashboardApp;
import com.medibook.hospital.appointmentinterface.doctor.DoctorDashboardApp;
import com.medibook.hospital.appointmentinterface.patient.PatientDashboardApp;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome");

        StackPane root = new StackPane();
        root.getStyleClass().add("root-pane");

        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setMaxWidth(800);

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.getStyleClass().add("welcome-label");

        HBox formsContainer = new HBox(40);
        formsContainer.setAlignment(Pos.CENTER);

        VBox loginPane = createLoginPane(primaryStage);
        VBox registerPane = createRegisterPane();
        VBox separator = createSeparator();

        formsContainer.getChildren().addAll(loginPane, separator, registerPane);
        mainContent.getChildren().addAll(welcomeLabel, formsContainer);
        root.getChildren().add(mainContent);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginPane(Stage ownerStage) {
        VBox pane = new VBox(15);
        pane.getStyleClass().add("form-pane");
        pane.getStyleClass().add("login-pane");
        pane.setPadding(new Insets(40));

        Label title = new Label("Login");
        title.getStyleClass().add("form-title");

        // Use the helper methods that return a StackPane
        Node usernameField = createIconTextField("Username", FontAwesomeIcon.USER);
        Node passwordField = createIconPasswordField("Password", FontAwesomeIcon.LOCK);

        Label roleLabel = new Label("Login As:");
        roleLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");
        ComboBox<String> roleSelector = new ComboBox<>();
        roleSelector.getItems().addAll("Patient", "Doctor", "Admin");
        roleSelector.setValue("Patient");
        roleSelector.setMaxWidth(Double.MAX_VALUE);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        loginButton.setOnAction(event -> {
            String selectedRole = roleSelector.getValue();
            if ("Admin".equals(selectedRole)) {
                launchDashboard(new AdminDashboardApp(), ownerStage);
            } else if ("Doctor".equals(selectedRole)) {
                launchDashboard(new DoctorDashboardApp(), ownerStage);
            } else {
                launchDashboard(new PatientDashboardApp(), ownerStage);
            }
        });

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");

        pane.getChildren().addAll(title, usernameField, passwordField, roleLabel, roleSelector, loginButton, forgotPasswordLink);
        return pane;
    }

    private void launchDashboard(Application dashboard, Stage loginStage) {
        Stage dashboardStage = new Stage();
        try {
            dashboard.start(dashboardStage);
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // THIS IS THE CODE YOU PROVIDED - IT'S PERFECT
    private VBox createRegisterPane() {
        VBox pane = new VBox(15);
        pane.getStyleClass().add("form-pane");
        pane.getStyleClass().add("register-pane");
        pane.setPadding(new Insets(40));

        Text title1 = new Text("New Patient?");
        Text title2 = new Text("Register Now");
        title1.getStyleClass().add("form-title");
        title2.getStyleClass().add("form-title");
        VBox titleBox = new VBox(-5, title1, title2);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));

        Node fullNameField = createIconTextField("Full Name", FontAwesomeIcon.USER);
        Node emailField = createIconTextField("Email", FontAwesomeIcon.ENVELOPE);
        Node createPasswordField = createIconPasswordField("Create Password", FontAwesomeIcon.LOCK);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        pane.getChildren().addAll(titleBox, fullNameField, emailField, createPasswordField, registerButton);
        return pane;
    }

    private VBox createSeparator() {
        VBox separatorPane = new VBox();
        separatorPane.setAlignment(Pos.CENTER);
        Label orLabel = new Label("OR");
        orLabel.getStyleClass().add("or-label");
        separatorPane.getChildren().add(orLabel);
        return separatorPane;
    }

    // --- UPDATED HELPER METHODS TO CREATE ICONS INSIDE TEXT FIELDS ---

    private Node createIconTextField(String prompt, FontAwesomeIcon iconName) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        return addIconToField(textField, iconName);
    }

    private Node createIconPasswordField(String prompt, FontAwesomeIcon iconName) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        return addIconToField(passwordField, iconName);
    }

    // This method now returns a StackPane containing both the field and the icon
    private StackPane addIconToField(Control field, FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.getStyleClass().add("text-field-icon");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(field, icon);
        StackPane.setAlignment(icon, Pos.CENTER_LEFT);
        StackPane.setMargin(icon, new Insets(0, 0, 0, 10)); // Indent the icon slightly

        // Add a style class to the text field itself to add left padding
        field.getStyleClass().add("icon-text-field");
        return stackPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}