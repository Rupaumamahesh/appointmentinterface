package com.medibook.hospital.appointmentinterface.auth;

import com.medibook.hospital.appointmentinterface.dao.DoctorDAO;
import com.medibook.hospital.appointmentinterface.dao.PatientDAO;
import com.medibook.hospital.appointmentinterface.dao.User;
import com.medibook.hospital.appointmentinterface.dao.UserDAO;
import com.medibook.hospital.appointmentinterface.doctor.view.DoctorPortalMainView;
import com.medibook.hospital.appointmentinterface.patient.PatientPortalMainView;
import com.medibook.hospital.appointmentinterface.admin.AdminDashboardApp;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LoginApp extends Application {

    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleSelector;
    private TextField fullNameField;
    private TextField emailField;
    private PasswordField createPasswordField;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Welcome to MediBook");
        showLoginScreen();
    }

    public void showLoginScreen() {
        // Use a standard VBox as the root for centering
        VBox root = new VBox();
        root.getStyleClass().add("root"); // Apply background to the scene root
        root.setAlignment(Pos.CENTER);

        VBox mainContent = createLoginScreenUI();
        root.getChildren().add(mainContent);

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // In LoginApp.java

    // In LoginApp.java

    private VBox createLoginScreenUI() {
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setMaxWidth(800);

        // --- START: CORRECTED CODE ---
        // The path is now simpler and absolute from the resources root.
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));

        // Defensive check to prevent crashes and give a helpful error message.
        if (logoImage.isError()) {
            System.err.println("Error: Could not load logo.png. Make sure it's in src/main/resources/");
        }

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(50);
        logoView.setPreserveRatio(true);

        Label appNameLabel = new Label("MediBook");
        appNameLabel.getStyleClass().add("app-name-label");

        HBox appHeader = new HBox(10, logoView, appNameLabel);
        appHeader.setAlignment(Pos.CENTER);
        // --- END OF CORRECTED CODE ---

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.getStyleClass().add("welcome-title");

        HBox formsContainer = new HBox(50);
        formsContainer.setAlignment(Pos.CENTER);
        formsContainer.getChildren().addAll(createLoginPane(), createRegisterPane());

        mainContent.getChildren().addAll(appHeader, welcomeLabel, formsContainer);

        VBox.setMargin(appHeader, new Insets(0, 0, 10, 0));
        VBox.setMargin(welcomeLabel, new Insets(0, 0, 20, 0));

        return mainContent;
    }

    private VBox createLoginPane() {
        VBox pane = new VBox(15);
        pane.getStyleClass().addAll("form-pane", "login-pane");
        pane.setPadding(new Insets(30));
        pane.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Login");
        title.getStyleClass().add("form-title");

        usernameField = new TextField();
        passwordField = new PasswordField();
        roleSelector = new ComboBox<>();
        roleSelector.getItems().addAll("Patient", "Doctor", "Admin");
        roleSelector.setValue("Patient");
        roleSelector.setMaxWidth(Double.MAX_VALUE);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> handleLogin());

        Button forgotPasswordLink = new Button("Forgot Password?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");

        VBox.setMargin(title, new Insets(0, 0, 10, 0));
        pane.getChildren().addAll(title,
                createIconTextField(usernameField, "Username", FontAwesomeIcon.USER),
                createIconTextField(passwordField, "Password", FontAwesomeIcon.LOCK),
                new Label("Login As:"), roleSelector, loginButton, forgotPasswordLink);
        return pane;
    }

    private VBox createRegisterPane() {
        VBox pane = new VBox(15);
        pane.getStyleClass().addAll("form-pane", "register-pane");
        pane.setPadding(new Insets(30));
        pane.setAlignment(Pos.CENTER_LEFT);

        Label titleMain = new Label("New Patient?");
        titleMain.getStyleClass().add("register-title-main");
        Label titleSub = new Label("Register Now");
        titleSub.getStyleClass().add("register-title-sub");
        VBox titleBox = new VBox(-5, titleMain, titleSub);

        fullNameField = new TextField();
        emailField = new TextField();
        createPasswordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(event -> handleRegistration());

        VBox.setMargin(titleBox, new Insets(0, 0, 10, 0));
        pane.getChildren().addAll(titleBox,
                createIconTextField(fullNameField, "Full Name", FontAwesomeIcon.USER),
                createIconTextField(emailField, "Email", FontAwesomeIcon.ENVELOPE),
                createIconTextField(createPasswordField, "Create Password", FontAwesomeIcon.LOCK),
                registerButton);
        return pane;
    }

    private StackPane createIconTextField(TextInputControl field, String prompt, FontAwesomeIcon iconName) {
        field.setPromptText(prompt);
        field.getStyleClass().add("icon-text-field");

        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.getStyleClass().add("input-icon");

        StackPane stackPane = new StackPane(field, icon);
        stackPane.setAlignment(Pos.CENTER_LEFT);
        return stackPane;
    }
    private void handleLogin() {
        UserDAO userDAO = new UserDAO();
        User loggedInUser = userDAO.validateLogin(usernameField.getText(), passwordField.getText(), roleSelector.getValue());

        if (loggedInUser != null) {
            switch (loggedInUser.getRole()) {
                case "Admin":
                    showAdminDashboard(loggedInUser.getId());
                    break;

                case "Doctor":
                    // --- THIS IS THE FIX ---
                    // We have the user_id, but the portal needs the doctor_id.
                    // Let's get the correct doctor_id from the DoctorDAO.
                    DoctorDAO doctorDAO = new DoctorDAO();
                    int doctorId = doctorDAO.getDoctorIdByUserId(loggedInUser.getId());

                    if (doctorId != -1) {
                        showDoctorDashboard(doctorId);
                    } else {
                        showAlert("Login Error", "Could not find a doctor profile associated with this user account.");
                    }
                    // --- END OF FIX ---
                    break;

                case "Patient":
                    // You will do the same thing here for patients later
                    showPatientDashboard(loggedInUser.getId());
                    break;
            }
        } else {
            showAlert("Login Failed", "Invalid username, password, or role.");
        }
    }

    // In: auth/LoginApp.java

    private void handleRegistration() {
        String fullName = fullNameField.getText();
        String email = emailField.getText(); // This is used as the username
        String password = createPasswordField.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Please fill in all fields.");
            return;
        }

        PatientDAO patientDAO = new PatientDAO();

        // --- THIS IS THE FIX ---
        // The method now returns an int (the new user's ID or -1)
        int newUserId = patientDAO.registerPatient(fullName, email, password);

        // Check if the returned ID is valid (not -1)
        if (newUserId != -1) {
            showAlert("Registration Successful", "Welcome! Please complete your profile.");
            // Automatically log in and redirect the new patient
            showPatientDashboard(newUserId);
        } else {
            showAlert("Registration Failed", "An account with this email may already exist.");
        }
    }

    private void showDoctorDashboard(int doctorId) {
        DoctorPortalMainView portalView = new DoctorPortalMainView(doctorId);
        Node portalUINode = portalView.getPortalView();
        portalView.loadInitialData();
        // --- THIS IS THE MISSING STEP ---
        // Tell the portal to load the data for its initial view (the dashboard)
        portalView.loadInitialData();
        // --- END OF MISSING STEP ---
        Scene scene = new Scene((Parent) portalUINode, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Doctor Portal");
    }
    private void showPatientDashboard(int userId) {
        // --- Translate user_id to patient_id ---
        PatientDAO patientDAO = new PatientDAO();
        int patientId = patientDAO.getPatientIdByUserId(userId);

        if (patientId != -1) {
            // Create an instance of the main patient portal shell
            PatientPortalMainView portalView = new PatientPortalMainView(patientId);

            // Get the complete portal UI
            Node portalUINode = portalView.getPortalView();

            // Set the scene
            Scene scene = new Scene((Parent) portalUINode, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Patient Portal");
        } else {
            showAlert("Login Error", "Could not find a patient profile associated with this user account.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (title.contains("Successful")) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAdminDashboard(int adminId) {
        // Create an instance of your main admin portal shell
        AdminDashboardApp portal = new AdminDashboardApp(adminId);

        // Get the complete portal UI Node
        Node portalUINode = portal.getPortalView();

        // Set the scene
        Scene scene = new Scene((Parent) portalUINode, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Portal");
    }
    public static void main(String[] args) {
        launch(args);
    }
}