// In: admin/AdminDashboardApp.java
package com.medibook.hospital.appointmentinterface.admin;

import com.medibook.hospital.appointmentinterface.admin.view.AdminDashboardView;
import com.medibook.hospital.appointmentinterface.admin.view.ManageDoctorsView;
import com.medibook.hospital.appointmentinterface.admin.view.ManagePatientsView;
import com.medibook.hospital.appointmentinterface.admin.view.MasterScheduleView;
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

public class AdminDashboardApp extends Application {

    private BorderPane mainLayout;
    private Button selectedNavButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Portal");

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

    private VBox createNavigationPanel() {
        VBox navPanel = new VBox(20);
        navPanel.setPadding(new Insets(20));
        navPanel.getStyleClass().add("nav-panel");
        navPanel.setAlignment(Pos.TOP_CENTER);

        Button dashboardButton = createNavButton("Dashboard", FontAwesomeIcon.HOME);
        Button doctorsButton = createNavButton("Manage Doctors", FontAwesomeIcon.USER_MD);
        Button patientsButton = createNavButton("Manage Patients", FontAwesomeIcon.USERS);
        Button scheduleButton = createNavButton("Master Schedule", FontAwesomeIcon.CALENDAR_PLUS_ALT);
        Button logoutButton = createNavButton("Logout", FontAwesomeIcon.SIGN_OUT);

        dashboardButton.setOnAction(e -> { navigateTo("Dashboard"); setSelected(dashboardButton); });
        doctorsButton.setOnAction(e -> { navigateTo("Manage Doctors"); setSelected(doctorsButton); });
        patientsButton.setOnAction(e -> { navigateTo("Manage Patients"); setSelected(patientsButton); });
        scheduleButton.setOnAction(e -> { navigateTo("Master Schedule"); setSelected(scheduleButton); });
        logoutButton.setOnAction(e -> System.out.println("Logout Clicked!"));

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        navPanel.getChildren().addAll(dashboardButton, doctorsButton, patientsButton, scheduleButton, spacer, logoutButton);
        setSelected(dashboardButton);
        return navPanel;
    }

    private void navigateTo(String page) {
        Node viewNode;
        switch (page) {
            case "Dashboard":
                viewNode = new AdminDashboardView().getView();
                break;
            case "Manage Doctors":
                viewNode = new ManageDoctorsView().getView();
                break;
            case "Manage Patients":
                viewNode = new ManagePatientsView().getView();
                break;
            case "Master Schedule":
                viewNode = new MasterScheduleView().getView();
                break;
            default:
                viewNode = new Label("Page not found");
                break;
        }
        viewNode.getStyleClass().add("card");
        mainLayout.setCenter(viewNode);
    }

    private Button createNavButton(String tooltipText, FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.setSize("24");
        Button button = new Button();
        button.setGraphic(icon);
        button.setTooltip(new Tooltip(tooltipText));
        button.getStyleClass().add("nav-button-icon-only");
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
}