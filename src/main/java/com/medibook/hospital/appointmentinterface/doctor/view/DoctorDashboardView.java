// In: doctor/view/DoctorDashboardView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DoctorDashboardView {
    public Node getView() {
        VBox view = new VBox(30);
        view.setPadding(new Insets(25));

        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");

        // --- Quick Stats Section ---
        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
                createStatCard("Total Appointments", "12"),
                createStatCard("Completed", "8"),
                createStatCard("Pending", "4")
        );

        // --- Urgent Notifications Section ---
        VBox notificationsBox = new VBox(10);
        Label notificationsTitle = new Label("Action Items");
        notificationsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        notificationsBox.getChildren().addAll(
                notificationsTitle,
                new Label("• Review 3 Urgent Lab Results"),
                new Label("• 1 New Message from Dr. Smith")
        );

        // --- Quick Links Section ---
        HBox quickLinksBox = new HBox(15);
        quickLinksBox.setAlignment(Pos.CENTER_LEFT);
        Button viewScheduleBtn = new Button("View Full Schedule");
        Button patientSearchBtn = new Button("Patient Search");
        viewScheduleBtn.getStyleClass().add("save-button");
        patientSearchBtn.getStyleClass().add("save-button");
        quickLinksBox.getChildren().addAll(viewScheduleBtn, patientSearchBtn);

        view.getChildren().addAll(title, statsBox, notificationsBox, quickLinksBox);
        return view;
    }

    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.getStyleClass().add("stat-card");
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #555;");
        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }
}