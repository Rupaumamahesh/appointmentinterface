package com.medibook.hospital.appointmentinterface.patient;

import com.medibook.hospital.appointmentinterface.model.Appointment; // Make sure this import is correct
import com.medibook.hospital.appointmentinterface.patient.view.MyProfileView;
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

public class PatientDashboardApp extends Application {

    private BorderPane mainLayout;
    private Button selectedNavButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Patient Portal");

        // Use StackPane as the root to layer the background
        StackPane root = new StackPane();
        root.getStyleClass().add("root-pane"); // This class will hold the background image

        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20)); // Add some padding around the content

        // Create and style the navigation panel
        VBox navPanel = createNavigationPanel();
        navPanel.getStyleClass().add("card"); // Style nav as a card
        mainLayout.setLeft(navPanel);
        BorderPane.setMargin(navPanel, new Insets(0, 20, 0, 0)); // Margin between nav and content

        // We will set the center content later in navigateTo()

        // Add the main layout on top of the background
        root.getChildren().add(mainLayout);

        // Set the initial view to the Dashboard
        navigateTo("Dashboard");

        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add(getClass().getResource("/com/medibook/hospital/appointmentinterface/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // In PatientDashboardApp.java

    private VBox createNavigationPanel() {
        VBox navPanel = new VBox(20); // Increased spacing
        navPanel.setPadding(new Insets(20));
        navPanel.getStyleClass().add("nav-panel");
        navPanel.setAlignment(Pos.TOP_CENTER); // Center icons vertically

        // Create icon-only buttons
        Button dashboardButton = createNavButton("Dashboard", FontAwesomeIcon.HOME);
        Button myAppointmentsButton = createNavButton("My Appointments", FontAwesomeIcon.CALENDAR);
        Button confirmAppointmentsButton = createNavButton("Confirm Appointments", FontAwesomeIcon.CHECK_SQUARE);
        Button myProfileButton = createNavButton("My Profile", FontAwesomeIcon.USER);
        Button logoutButton = createNavButton("Logout", FontAwesomeIcon.SIGN_OUT);

        // Add tooltips to show text on hover
        dashboardButton.setTooltip(new Tooltip("Dashboard"));
        myAppointmentsButton.setTooltip(new Tooltip("My Appointments"));
        confirmAppointmentsButton.setTooltip(new Tooltip("Confirm Appointments"));
        myProfileButton.setTooltip(new Tooltip("My Profile"));
        logoutButton.setTooltip(new Tooltip("Logout"));

        // Event handlers remain the same
        dashboardButton.setOnAction(e -> { navigateTo("Dashboard"); setSelected(dashboardButton); });
        myAppointmentsButton.setOnAction(e -> { navigateTo("My Appointments"); setSelected(myAppointmentsButton); });
        confirmAppointmentsButton.setOnAction(e -> { navigateTo("Confirm Appointments"); setSelected(confirmAppointmentsButton); });
        myProfileButton.setOnAction(e -> { navigateTo("My Profile"); setSelected(myProfileButton); });
        logoutButton.setOnAction(e -> System.out.println("Logout Clicked!"));

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        navPanel.getChildren().addAll(dashboardButton, myAppointmentsButton, confirmAppointmentsButton, myProfileButton, spacer, logoutButton);
        setSelected(dashboardButton);
        return navPanel;
    }

    private Button createNavButton(String tooltipText, FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.setSize("24"); // Make icons a bit larger

        Button button = new Button(); // Create button with NO text
        button.setGraphic(icon);
        button.getStyleClass().add("nav-button-icon-only"); // Use a new style class
        button.setTooltip(new Tooltip(tooltipText)); // Add a hover tooltip
        return button;
    }

    private void setSelected(Button button) {
        if (selectedNavButton != null) {
            selectedNavButton.getStyleClass().remove("selected");
        }
        selectedNavButton = button;
        if (selectedNavButton != null) {
            selectedNavButton.getStyleClass().add("selected");
        }
    }

    // Main navigation logic
    private void navigateTo(String page) {
        Node viewNode;
        switch (page) {
            case "Dashboard":
                viewNode = new DashboardView().getView();
                break;
            case "My Appointments":
                viewNode = new MyAppointmentsView().getView();
                break;
            case "Confirm Appointments":
                viewNode = createPlaceholderView("Confirm Appointments");
                break;
            case "My Profile":
                viewNode = new MyProfileView().getView();
                break;
            default:
                viewNode = createPlaceholderView("Not Found");
                break;
        }

        // Add the card style to the main content area
        viewNode.getStyleClass().add("card");

        mainLayout.setCenter(viewNode);
    }

    private Node createPlaceholderView(String title) {
        VBox placeholder = new VBox();
        placeholder.setPadding(new Insets(20));
        Label label = new Label(title + " Page");
        label.getStyleClass().add("page-title");
        placeholder.getChildren().add(label);
        return placeholder;
    }
}