package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.DoctorDAO;
import com.medibook.hospital.appointmentinterface.model.Doctor;
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

public class DoctorPortalMainView {

    private final BorderPane mainLayout;
    private final int loggedInDoctorId;
    private final Doctor loggedInDoctor;

    // This is the single instance of the dashboard we will create and reuse
    private final DoctorDashboardView dashboardView;

    // UI Components
    private Button activeButton;
    private Button dashboardBtn;
    private Button scheduleBtn;
    private Button patientsBtn;
    private Button messagesBtn;
    private Button tasksBtn;
    private Button availabilityBtn;
    private Button settingsBtn;
    private Button logoutBtn;

    public DoctorPortalMainView(int doctorId) {
        this.loggedInDoctorId = doctorId;
        DoctorDAO doctorDAO = new DoctorDAO();
        this.loggedInDoctor = doctorDAO.getDoctorById(doctorId);

        if (this.loggedInDoctor == null) {
            System.err.println("FATAL ERROR: Could not load doctor with ID: " + doctorId);
            showAlertAndExit("Fatal Error", "Could not load doctor data. The application will now close.");
        }

        this.mainLayout = new BorderPane();
        // --- Create the dashboard instance ONCE and store it in the field ---
        this.dashboardView = new DoctorDashboardView();
        initializePortal();
    }

    private void initializePortal() {
        mainLayout.getStyleClass().add("root-pane");
        Node sideNav = createSideNavigationBar();
        mainLayout.setLeft(sideNav);

        // --- Set the initial view to the dashboard using our new helper method ---
        showDashboard();

        // --- Load the initial data for the dashboard AFTER it has been shown ---
        dashboardView.loadDashboardData(loggedInDoctorId);
    }

    /**
     * NEW METHOD: This handles showing the dashboard.
     * It reuses the existing dashboardView instance to preserve its state.
     */
    private void showDashboard() {
        Runnable scheduleAction = () -> switchView(new DoctorScheduleView().getView(loggedInDoctorId), scheduleBtn);
        Runnable patientSearchAction = () -> switchView(new PatientListView().getView(loggedInDoctorId), patientsBtn);

        // Get the view Node from the single, stored dashboardView instance
        Node dashboardContent = dashboardView.getView(scheduleAction, patientSearchAction);

        switchView(dashboardContent, dashboardBtn);
    }

    private Node createSideNavigationBar() {
        VBox sideNav = new VBox();
        sideNav.getStyleClass().add("side-navigation-bar");

        VBox topNavButtons = new VBox(10);
        dashboardBtn = createNavButton(FontAwesomeIcon.HOME);
        scheduleBtn = createNavButton(FontAwesomeIcon.CALENDAR);
        patientsBtn = createNavButton(FontAwesomeIcon.USERS);
        messagesBtn = createNavButton(FontAwesomeIcon.ENVELOPE);
        tasksBtn = createNavButton(FontAwesomeIcon.LIST_ALT);
        availabilityBtn = createNavButton(FontAwesomeIcon.CLOCK_ALT);
        topNavButtons.getChildren().addAll(dashboardBtn, scheduleBtn, patientsBtn, messagesBtn, tasksBtn, availabilityBtn);

        // --- UPDATED BUTTON ACTIONS ---
        // The dashboard button now calls the dedicated showDashboard method, ensuring the view is reused.
        dashboardBtn.setOnAction(e -> showDashboard());

        scheduleBtn.setOnAction(e -> switchView(new DoctorScheduleView().getView(loggedInDoctorId), scheduleBtn));
        patientsBtn.setOnAction(e -> switchView(new PatientListView().getView(loggedInDoctorId), patientsBtn));
        messagesBtn.setOnAction(e -> switchView(new SecureMessagingView(loggedInDoctor).getView(), messagesBtn));
        tasksBtn.setOnAction(e -> switchView(new TasksView().getView(loggedInDoctorId), tasksBtn));
        availabilityBtn.setOnAction(e -> switchView(new AvailabilityView().getView(loggedInDoctorId), availabilityBtn));

        VBox bottomNavButtons = new VBox(10);
        settingsBtn = createNavButton(FontAwesomeIcon.GEAR);
        logoutBtn = createNavButton(FontAwesomeIcon.SIGN_OUT);
        bottomNavButtons.getChildren().addAll(settingsBtn, logoutBtn);

        settingsBtn.setOnAction(e -> switchView(new DoctorProfileView().getView(loggedInDoctorId), settingsBtn));
        logoutBtn.setOnAction(e -> handleLogout());

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideNav.getChildren().addAll(topNavButtons, spacer, bottomNavButtons);
        return sideNav;
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
        if (clickedButton != null) {
            setActiveButton(clickedButton);
        }
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("nav-button-active");
        }
        button.getStyleClass().add("nav-button-active");
        activeButton = button;
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

    public Node getPortalView() {
        return mainLayout;
    }

    // This method is no longer needed, as loading is handled in initializePortal
    // public void loadInitialData() { ... }

    private void showAlertAndExit(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        javafx.application.Platform.exit();
    }
}