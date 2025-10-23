package com.medibook.hospital.appointmentinterface.doctor.view;

// --- STEP 1: ADD THE MISSING IMPORT STATEMENTS ---
import com.medibook.hospital.appointmentinterface.dao.DoctorDAO;
import com.medibook.hospital.appointmentinterface.model.Doctor;
// --- END OF STEP 1 ---

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
    private final DoctorDashboardView dashboardView;

    // --- STEP 2: DECLARE THE NEW FIELD FOR THE DOCTOR OBJECT ---
    private final Doctor loggedInDoctor;
    // --- END OF STEP 2 ---

    // --- UI Components as Class Fields ---
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

        // Fetch the full doctor object and store it in the new field
        DoctorDAO doctorDAO = new DoctorDAO();
        this.loggedInDoctor = doctorDAO.getDoctorById(doctorId);

        // Handle case where doctor might not be found in the database
        if (this.loggedInDoctor == null) {
            // This is a critical error, the portal cannot function without doctor data
            System.err.println("FATAL ERROR: Could not load doctor with ID: " + doctorId);
            // In a real application, you'd show a proper error dialog and likely exit
            showAlertAndExit("Fatal Error", "Could not load doctor data. The application will now close.");
        }

        this.mainLayout = new BorderPane();
        this.dashboardView = new DoctorDashboardView(); // Create a single instance
        initializePortal();
    }

    private void initializePortal() {
        mainLayout.getStyleClass().add("root-pane");

        // Create the navigation bar
        Node sideNav = createSideNavigationBar();
        mainLayout.setLeft(sideNav);

        // Define the actions for the dashboard buttons
        Runnable scheduleAction = () -> switchView(new DoctorScheduleView().getView(loggedInDoctorId), scheduleBtn);
        Runnable patientSearchAction = () -> switchView(new PatientListView().getView(loggedInDoctorId), patientsBtn);

        // Get the initial dashboard view and pass the actions to it
        Node dashboardContent = dashboardView.getView(scheduleAction, patientSearchAction);

        // Set the initial view
        switchView(dashboardContent, dashboardBtn);

        // After the view is set, load its data.
        dashboardView.loadDashboardData(loggedInDoctorId);
    }

    private Node createSideNavigationBar() {
        VBox sideNav = new VBox();
        sideNav.getStyleClass().add("side-navigation-bar");

        // Top Buttons
        VBox topNavButtons = new VBox(10);
        dashboardBtn = createNavButton(FontAwesomeIcon.HOME);
        scheduleBtn = createNavButton(FontAwesomeIcon.CALENDAR);
        patientsBtn = createNavButton(FontAwesomeIcon.USERS);
        messagesBtn = createNavButton(FontAwesomeIcon.ENVELOPE);
        tasksBtn = createNavButton(FontAwesomeIcon.LIST_ALT);
        availabilityBtn = createNavButton(FontAwesomeIcon.CLOCK_ALT);
        topNavButtons.getChildren().addAll(dashboardBtn, scheduleBtn, patientsBtn, messagesBtn, tasksBtn, availabilityBtn);

        // Actions for Top Buttons
        dashboardBtn.setOnAction(e -> {
            Runnable scheduleAction = () -> switchView(new DoctorScheduleView().getView(loggedInDoctorId), scheduleBtn);
            Runnable patientSearchAction = () -> switchView(new PatientListView().getView(loggedInDoctorId), patientsBtn);
            switchView(dashboardView.getView(scheduleAction, patientSearchAction), dashboardBtn);
        });
        scheduleBtn.setOnAction(e -> switchView(new DoctorScheduleView().getView(loggedInDoctorId), scheduleBtn));
        patientsBtn.setOnAction(e -> switchView(new PatientListView().getView(loggedInDoctorId), patientsBtn));

        // --- STEP 3: THE CORRECTED LINE FOR THE MESSAGES BUTTON ---
        messagesBtn.setOnAction(e -> switchView(new SecureMessagingView(loggedInDoctor).getView(), messagesBtn));
        // --- END OF STEP 3 ---

        tasksBtn.setOnAction(e -> switchView(new TasksView().getView(loggedInDoctorId), tasksBtn));
        availabilityBtn.setOnAction(e -> switchView(new AvailabilityView().getView(loggedInDoctorId), availabilityBtn));

        // Bottom Buttons
        VBox bottomNavButtons = new VBox(10);
        settingsBtn = createNavButton(FontAwesomeIcon.GEAR);
        logoutBtn = createNavButton(FontAwesomeIcon.SIGN_OUT);
        bottomNavButtons.getChildren().addAll(settingsBtn, logoutBtn);

        // Actions for Bottom Buttons
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

    public void loadInitialData() {
        if (dashboardView != null) {
            dashboardView.loadDashboardData(loggedInDoctorId);
        }
    }

    private void showAlertAndExit(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        javafx.application.Platform.exit();
    }
}