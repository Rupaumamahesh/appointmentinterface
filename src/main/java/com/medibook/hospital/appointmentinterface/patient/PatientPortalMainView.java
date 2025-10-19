// In: patient/PatientPortalMainView.java
package com.medibook.hospital.appointmentinterface.patient;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

// You may need these imports
import com.medibook.hospital.appointmentinterface.patient.view.PatientDashboardView;
import com.medibook.hospital.appointmentinterface.patient.view.MyProfileView;


public class PatientPortalMainView {

    private BorderPane mainLayout;
    private int loggedInPatientId;
    private MyAppointmentsView myAppointmentsView; // Keep a reference to this view

    // --- 1. Add this variable to track the active button ---
    private Button activeButton;

    public PatientPortalMainView(int patientId) {
        this.loggedInPatientId = patientId;
        this.mainLayout = new BorderPane();
        initializePortal();
    }

    private void initializePortal() {
        mainLayout.getStyleClass().add("root-pane");

        Node sideNav = createSideNavigationBar();
        mainLayout.setLeft(sideNav);

        this.myAppointmentsView = new MyAppointmentsView();

        // --- 2. Correct the initial view loading ---
        // Get the VBox containing the top buttons
        VBox topNavButtons = (VBox) ((VBox) sideNav).getChildren().get(0);
        // The "Appointments" button is the second one (index 1)
        Button appointmentsButton = (Button) topNavButtons.getChildren().get(1);

        // Load the appointments view and set its button as active
        switchView(myAppointmentsView.getView(), appointmentsButton);
    }

    private Node createSideNavigationBar() {
        VBox sideNav = new VBox();
        sideNav.getStyleClass().add("side-navigation-bar");

        VBox topNavButtons = new VBox(10); // Container for top buttons

        // Create a button for each patient view
        Button dashboardBtn = createNavButton(FontAwesomeIcon.HOME);
        Button appointmentsBtn = createNavButton(FontAwesomeIcon.CALENDAR);
        Button profileBtn = createNavButton(FontAwesomeIcon.USER);

        topNavButtons.getChildren().addAll(dashboardBtn, appointmentsBtn, profileBtn);

        // --- Set actions for the buttons ---
        dashboardBtn.setOnAction(e -> switchView(new PatientDashboardView().getView(), dashboardBtn));
        appointmentsBtn.setOnAction(e -> switchView(myAppointmentsView.getView(), appointmentsBtn));
        profileBtn.setOnAction(e -> switchView(new MyProfileView().getView(), profileBtn));

        // --- 3. Correct the layout for the bottom button ---
        VBox bottomNavButtons = new VBox(10); // Container for bottom buttons
        Button logoutBtn = createNavButton(FontAwesomeIcon.SIGN_OUT);
        bottomNavButtons.getChildren().add(logoutBtn);

        Pane spacer = new Pane(); // This pushes the bottom buttons down
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

    // --- 4. Update switchView to accept a Button parameter ---
    private void switchView(Node newView, Button clickedButton) {
        newView.getStyleClass().add("content-pane");
        mainLayout.setCenter(newView);
        setActiveButton(clickedButton);
    }

    // --- 5. Add the setActiveButton method ---
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

    public void loadInitialData() {
        if (myAppointmentsView != null) {
            myAppointmentsView.loadAppointments(loggedInPatientId);
        }
    }
}