package com.medibook.hospital.appointmentinterface.admin;

import com.medibook.hospital.appointmentinterface.admin.view.AdminDashboardView;
import com.medibook.hospital.appointmentinterface.admin.view.ManageDoctorsView;
import com.medibook.hospital.appointmentinterface.admin.view.ManagePatientsView;
import com.medibook.hospital.appointmentinterface.admin.view.MasterScheduleView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdminDashboardApp {

    private final BorderPane mainLayout;
    private final AdminDashboardView dashboardView;

    private Button activeButton;
    private Button dashboardBtn;
    private Button doctorsBtn;
    private Button patientsBtn;
    private Button scheduleBtn;

    public AdminDashboardApp(int adminId) {
        this.mainLayout = new BorderPane();
        this.dashboardView = new AdminDashboardView();
        initializePortal();
    }

    private void initializePortal() {
        mainLayout.getStyleClass().add("root-pane");
        Node sideNav = createSideNavigationBar();
        mainLayout.setLeft(sideNav);
        showDashboard();
    }

    private void showDashboard() {
        Runnable manageDoctorsAction = () -> switchView(new ManageDoctorsView().getView(), doctorsBtn);
        Runnable managePatientsAction = () -> switchView(new ManagePatientsView().getView(), patientsBtn);
        Runnable masterScheduleAction = () -> switchView(new MasterScheduleView().getView(), scheduleBtn);

        Node dashboardNode = dashboardView.getView(manageDoctorsAction, managePatientsAction, masterScheduleAction);
        dashboardView.loadDashboardData();
        switchView(dashboardNode, dashboardBtn);
    }

    private Node createSideNavigationBar() {
        VBox sideNav = new VBox();
        sideNav.getStyleClass().add("side-navigation-bar");

        VBox topNavButtons = new VBox(10);
        dashboardBtn = createNavButton(FontAwesomeIcon.TACHOMETER);
        doctorsBtn = createNavButton(FontAwesomeIcon.USER_MD);
        patientsBtn = createNavButton(FontAwesomeIcon.USERS);
        scheduleBtn = createNavButton(FontAwesomeIcon.CALENDAR);
        topNavButtons.getChildren().addAll(dashboardBtn, doctorsBtn, patientsBtn, scheduleBtn);

        dashboardBtn.setOnAction(e -> showDashboard());
        doctorsBtn.setOnAction(e -> switchView(new ManageDoctorsView().getView(), doctorsBtn));
        patientsBtn.setOnAction(e -> switchView(new ManagePatientsView().getView(), patientsBtn));
        scheduleBtn.setOnAction(e -> switchView(new MasterScheduleView().getView(), scheduleBtn));

        VBox bottomNavButtons = new VBox(10);
        Button logoutBtn = createNavButton(FontAwesomeIcon.SIGN_OUT);
        bottomNavButtons.getChildren().add(logoutBtn);

        // --- THIS IS THE FIX ---
        // The button now calls the method that shows the confirmation dialog.
        logoutBtn.setOnAction(e -> handleLogout());
        // --- END OF FIX ---

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideNav.getChildren().addAll(topNavButtons, spacer, bottomNavButtons);
        return sideNav;
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.out.println("Logging out...");
                javafx.application.Platform.exit();
            }
        });
    }

    private Button createNavButton(FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.getStyleClass().add("glyph-icon");
        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("nav-button");
        return button;
    }

    private void switchView(Node newView, Button clickedButton) {
        newView.getStyleClass().add("content-pane");
        mainLayout.setCenter(newView);
        setActiveButton(clickedButton);
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("nav-button-active");
        }
        button.getStyleClass().add("nav-button-active");
        activeButton = button;
    }

    public Node getPortalView() {
        return mainLayout;
    }
}