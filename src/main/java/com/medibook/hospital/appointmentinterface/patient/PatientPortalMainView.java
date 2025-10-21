// In: patient/PatientPortalMainView.java
package com.medibook.hospital.appointmentinterface.patient;

import com.medibook.hospital.appointmentinterface.dao.PatientDAO;
import com.medibook.hospital.appointmentinterface.model.Doctor;
import com.medibook.hospital.appointmentinterface.model.Patient;
import com.medibook.hospital.appointmentinterface.patient.view.MyAppointmentsView;
import com.medibook.hospital.appointmentinterface.patient.view.MyProfileView;
import com.medibook.hospital.appointmentinterface.patient.view.PatientDashboardView;
import com.medibook.hospital.appointmentinterface.patient.view.DoctorSearchView;
import com.medibook.hospital.appointmentinterface.patient.view.DoctorScheduleSelectionView;
import com.medibook.hospital.appointmentinterface.patient.view.AppointmentConfirmationView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class PatientPortalMainView {

    private final BorderPane mainLayout;
    private final int loggedInPatientId;
    private final Patient loggedInPatient;

    private Button activeButton;
    private Button dashboardBtn;
    private Button appointmentsBtn;
    private Button profileBtn;

    public PatientPortalMainView(int patientId) {
        this.loggedInPatientId = patientId;
        PatientDAO patientDAO = new PatientDAO();
        this.loggedInPatient = patientDAO.getPatientById(patientId);
        this.mainLayout = new BorderPane();
        initializePortal();
    }

    private void initializePortal() {
        mainLayout.getStyleClass().add("root-pane");
        Node sideNav = createSideNavigationBar();
        mainLayout.setLeft(sideNav);
        showDashboard();
    }

    // --- NAVIGATION METHODS ---

    // In: PatientPortalMainView.java

    // In: patient/PatientPortalMainView.java

    private void showDashboard() {
        // --- Define the action for the "Book a New Appointment" button ---
        Runnable bookAppointmentAction = () -> {
            // --- THIS IS THE NEW LOGIC ---
            // 1. Check if the patient's profile is complete before proceeding.
            PatientDAO dao = new PatientDAO();
            if (dao.isProfileComplete(loggedInPatientId)) {
                // 2. If profile is complete, proceed with the booking workflow.

                // Define what happens when a doctor is selected from the search results
                Consumer<Doctor> onDoctorSelected = doctor -> {
                    DoctorScheduleSelectionView scheduleView = new DoctorScheduleSelectionView(doctor);

                    // Declare a final array to hold the onSlotSelected consumer, allowing it to be used inside onGoBack
                    final Consumer<DoctorScheduleSelectionView.AppointmentSelection>[] onSlotSelected = new Consumer[1];

                    // Define the "Go Back" action for the confirmation page
                    Runnable onGoBack = () -> switchView(scheduleView.getView(onSlotSelected[0]), null);

                    // Define what happens when a time slot is selected from the schedule view
                    onSlotSelected[0] = selection -> {
                        Runnable onBookingConfirmed = () -> showMyAppointments();

                        AppointmentConfirmationView confirmationView = new AppointmentConfirmationView(selection, loggedInPatientId);
                        switchView(confirmationView.getView(onBookingConfirmed, onGoBack), null);
                    };

                    // Show the initial doctor schedule view
                    switchView(scheduleView.getView(onSlotSelected[0]), null);
                };

                // Show the doctor search view
                switchView(new DoctorSearchView().getView(onDoctorSelected), null);

            } else {
                // 3. If profile is incomplete, show a warning and redirect to the profile page.
                showAlert("Profile Incomplete", "Please complete your profile (Date of Birth and Phone Number) before booking an appointment.");
                showMyProfile(); // Navigate the user to their profile
            }
            // --- END OF NEW LOGIC ---
        };

        // --- Define the actions for the other dashboard buttons ---
        Runnable viewAppointmentsAction = () -> showMyAppointments();
        Runnable viewProfileAction = ()-> showMyProfile();

        // Create the dashboard view, passing in all the actions
        Node dashboardNode = new PatientDashboardView().getView(
                loggedInPatient.getFullName(),
                bookAppointmentAction,
                viewAppointmentsAction,
                viewProfileAction
        );

        // Set the dashboard as the current view
        switchView(dashboardNode, dashboardBtn);
    }
    private void showMyAppointments() {
        switchView(new MyAppointmentsView().getView(loggedInPatientId), appointmentsBtn);
    }

    private void showMyProfile() {
        switchView(new MyProfileView().getView(loggedInPatientId), profileBtn);
    }

    private Node createSideNavigationBar() {
        VBox sideNav = new VBox();
        sideNav.getStyleClass().add("side-navigation-bar");
        VBox topNavButtons = new VBox(10);

        dashboardBtn = createNavButton(FontAwesomeIcon.HOME);
        appointmentsBtn = createNavButton(FontAwesomeIcon.CALENDAR_CHECK_ALT);
        profileBtn = createNavButton(FontAwesomeIcon.USER);
        topNavButtons.getChildren().addAll(dashboardBtn, appointmentsBtn, profileBtn);

        dashboardBtn.setOnAction(e -> showDashboard());
        appointmentsBtn.setOnAction(e -> showMyAppointments());
        profileBtn.setOnAction(e -> showMyProfile());

        VBox bottomNavButtons = new VBox(10);
        Button logoutBtn = createNavButton(FontAwesomeIcon.SIGN_OUT);
        bottomNavButtons.getChildren().add(logoutBtn);
        logoutBtn.setOnAction(e -> javafx.application.Platform.exit());

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideNav.getChildren().addAll(topNavButtons, spacer, bottomNavButtons);
        return sideNav;
    }

    // --- HELPER METHODS ---

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

    private Button createNavButton(FontAwesomeIcon iconName) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.getStyleClass().add("glyph-icon");
        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("nav-button");
        return button;
    }

    public Node getPortalView() {
        return mainLayout;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}