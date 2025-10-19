// In: doctor/view/DoctorDashboardView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.AppointmentDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DoctorDashboardView {

    private Label totalAppointmentsValueLabel;
    private Label completedAppointmentsValueLabel;
    private Label pendingAppointmentsValueLabel;

    private AppointmentDAO appointmentDAO;

    public DoctorDashboardView() {
        // Initialize the DAO in the constructor
        this.appointmentDAO = new AppointmentDAO();
    }

    /**
     * Creates the dashboard view.
     * @param onScheduleClick Action to run when "View Full Schedule" is clicked.
     * @param onPatientSearchClick Action to run when "Patient Search" is clicked.
     * @return The complete dashboard UI Node.
     */
    public Node getView(Runnable onScheduleClick, Runnable onPatientSearchClick) {
        // Main container for the entire dashboard content
        VBox view = new VBox(30); // 30px spacing between vertical sections

        // --- Title ---
        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");

        // --- Quick Stats Section ---
        HBox statsBox = createStatsBox();

        // --- Action Items Section (IMPROVED UI) ---
        Node actionItemsBox = createActionItemsSection();

        // --- Quick Links Section (NOW FUNCTIONAL) ---
        Node quickLinksBox = createQuickLinksSection(onScheduleClick, onPatientSearchClick);

        // Add all sections in order to the main vertical layout
        view.getChildren().addAll(title, statsBox, actionItemsBox, quickLinksBox);
        return view;
    }

    /**
     * Creates the top section with statistics cards.
     */
    private HBox createStatsBox() {
        HBox statsBox = new HBox(20); // 20px spacing between stat cards
        totalAppointmentsValueLabel = new Label("...");
        completedAppointmentsValueLabel = new Label("...");
        pendingAppointmentsValueLabel = new Label("...");
        statsBox.getChildren().addAll(
                createStatCard("Total Appointments", totalAppointmentsValueLabel),
                createStatCard("Completed", completedAppointmentsValueLabel),
                createStatCard("Pending", pendingAppointmentsValueLabel)
        );
        return statsBox;
    }

    /**
     * Creates the visually improved "Action Items" section with icons.
     */
    private Node createActionItemsSection() {
        VBox actionItemsContainer = new VBox(15);
        Label actionItemsTitle = new Label("Action Items");
        actionItemsTitle.getStyleClass().add("section-title");

        // Create individual action items using a helper method
        Node labResultsItem = createActionItem(FontAwesomeIcon.FLASK, "Review 3 Urgent Lab Results");
        Node messageItem = createActionItem(FontAwesomeIcon.ENVELOPE, "1 New Message from Dr. Smith");

        actionItemsContainer.getChildren().addAll(actionItemsTitle, labResultsItem, messageItem);
        return actionItemsContainer;
    }

    /**
     * Helper method to create a single styled action item row with an icon.
     */
    private Node createActionItem(FontAwesomeIcon iconName, String text) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconName);
        icon.getStyleClass().add("action-item-icon");

        Label label = new Label(text);
        label.getStyleClass().add("action-item-label");

        HBox itemBox = new HBox(15); // 15px spacing between icon and text
        itemBox.getChildren().addAll(icon, label);
        return itemBox;
    }

    /**
     * Creates the bottom section with navigation buttons and wires them up.
     */
    private Node createQuickLinksSection(Runnable onScheduleClick, Runnable onPatientSearchClick) {
        HBox quickLinksBox = new HBox(15);
        Button viewScheduleBtn = new Button("View Full Schedule");
        Button patientSearchBtn = new Button("Patient Search");

        // Use consistent style classes from your styles.css
        viewScheduleBtn.getStyleClass().add("action-button");
        patientSearchBtn.getStyleClass().add("action-button-secondary");

        // --- FIX: Wire up the buttons to the provided actions ---
        viewScheduleBtn.setOnAction(e -> onScheduleClick.run());
        patientSearchBtn.setOnAction(e -> onPatientSearchClick.run());

        quickLinksBox.getChildren().addAll(viewScheduleBtn, patientSearchBtn);
        return quickLinksBox;
    }

    /**
     * Loads dynamic data from the database and updates the UI labels.
     */
    // In DoctorDashboardView.java

    public void loadDashboardData(int doctorId) {
        // --- THIS IS THE FIX ---
        // Use the correct method names that exist in your AppointmentDAO
        int totalCount = appointmentDAO.getTotalAppointmentsToday(doctorId);
        int completedCount = appointmentDAO.getAppointmentsByStatusToday(doctorId, "Completed");

        // Calculate pending from the results for today
        int pendingCount = totalCount - completedCount;

        // Update the UI labels
        totalAppointmentsValueLabel.setText(String.valueOf(totalCount));
        completedAppointmentsValueLabel.setText(String.valueOf(completedCount));
        pendingAppointmentsValueLabel.setText(String.valueOf(pendingCount));
    }

    /**
     * Helper method to create a single reusable statistics card.
     */
    private VBox createStatCard(String title, Label valueLabel) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        valueLabel.getStyleClass().add("stat-value-label");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title-label");
        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }
}