package com.medibook.hospital.appointmentinterface.doctor;

import com.medibook.hospital.appointmentinterface.doctor.view.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DoctorDashboardApp extends Application {

    private BorderPane mainLayout;
    private Button selectedNavButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Doctor Portal");

        StackPane root = new StackPane();
        root.getStyleClass().add("root-pane");

        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        VBox navPanel = createNavigationPanel();
        navPanel.getStyleClass().add("card");
        mainLayout.setLeft(navPanel);
        BorderPane.setMargin(navPanel, new Insets(0, 20, 0, 0));

        root.getChildren().add(mainLayout);

        navigateTo("Dashboard");

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- METHOD UPDATED for Icon-Only View ---
    // In DoctorDashboardApp.java

    private VBox createNavigationPanel() {
        VBox navPanel = new VBox(20);
        navPanel.setPadding(new Insets(20));
        navPanel.getStyleClass().add("nav-panel");
        navPanel.setAlignment(Pos.TOP_CENTER);

        // --- Define ALL buttons first ---
        Button dashboardButton = createNavButton("Dashboard", FontAwesomeIcon.HOME);
        Button scheduleButton = createNavButton("Schedule", FontAwesomeIcon.CALENDAR);
        Button patientsButton = createNavButton("Patients", FontAwesomeIcon.USERS);
        Button messagesButton = createNavButton("Messages", FontAwesomeIcon.ENVELOPE);
        Button tasksButton = createNavButton("Tasks", FontAwesomeIcon.LIST_ALT);
        Button availabilityButton = createNavButton("Availability", FontAwesomeIcon.CLOCK_ALT);
        Button settingsButton = createNavButton("Settings", FontAwesomeIcon.GEAR);
        Button logoutButton = createNavButton("Logout", FontAwesomeIcon.SIGN_OUT); // Only one definition

        // --- Set ALL actions ---
        dashboardButton.setOnAction(e -> { navigateTo("Dashboard"); setSelected(dashboardButton); });
        scheduleButton.setOnAction(e -> { navigateTo("Schedule"); setSelected(scheduleButton); });
        patientsButton.setOnAction(e -> { navigateTo("Patients"); setSelected(patientsButton); });
        messagesButton.setOnAction(e -> { navigateTo("Messages"); setSelected(messagesButton); });
        tasksButton.setOnAction(e -> { navigateTo("Tasks"); setSelected(tasksButton); });
        availabilityButton.setOnAction(e -> { navigateTo("Availability"); setSelected(availabilityButton); });
        settingsButton.setOnAction(e -> { navigateTo("Settings"); setSelected(settingsButton); });
        logoutButton.setOnAction(e -> System.out.println("Logout Clicked!"));

        // --- Define the spacer BEFORE using it ---
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // --- Add ALL children to the panel in the correct order ---
        navPanel.getChildren().addAll(
                dashboardButton,
                scheduleButton,
                patientsButton,
                messagesButton,
                tasksButton,
                availabilityButton,
                spacer, // Spacer is now correctly placed here
                settingsButton,
                logoutButton
        );

        setSelected(dashboardButton);
        return navPanel;
    }

    private void navigateTo(String page) {
        Node viewNode;
        switch (page) {
            case "Dashboard":
                viewNode = new DoctorDashboardView().getView();
                break;
            case "Schedule":
                viewNode = new DoctorScheduleView().getView();
                break;
            case "Patients":
                viewNode = new PatientListView().getView();
                break;
            // In DoctorDashboardApp.java -> navigateTo()

// ... (after the 'Patients' case)
            case "Messages":
                viewNode = new SecureMessagingView().getView();
                break;
            case "Tasks":
                viewNode = new TasksView().getView();
                break;
            case "Availability":
                viewNode = new AvailabilityView().getView();
                break;
            case "Settings":
                viewNode = new DoctorProfileView().getView();
                break;
// ...
            default:
                viewNode = new Label("Page not found");
                break;
        }
        viewNode.getStyleClass().add("card");
        mainLayout.setCenter(viewNode);
    }

    // --- METHOD UPDATED for Icon-Only View ---
    private Button createNavButton(String tooltipText, FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.setSize("24"); // Slightly larger icon for better visibility

        Button button = new Button(); // Button is created without text
        button.setGraphic(icon);
        button.setTooltip(new Tooltip(tooltipText)); // Add a hover tooltip for accessibility
        button.getStyleClass().add("nav-button-icon-only"); // Use the new specific CSS class
        return button;
    }

    // --- METHOD UNCHANGED ---
    private void setSelected(Button button) {
        if (selectedNavButton != null) {
            selectedNavButton.getStyleClass().remove("selected");
        }
        selectedNavButton = button;
        if (selectedNavButton != null) {
            selectedNavButton.getStyleClass().add("selected");
        }
    }
}