// In: admin/view/AdminDashboardView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AdminDashboardView {
    public Node getView() {
        VBox view = new VBox(30);
        view.setPadding(new Insets(25));

        Label title = new Label("Administrative Dashboard");
        title.getStyleClass().add("page-title");

        // --- KPIs for Today ---
        HBox kpiBox = new HBox(20);
        kpiBox.getChildren().addAll(
                createStatCard("Total Appointments", "152"),
                createStatCard("Completed", "110"),
                createStatCard("No-shows", "8"),
                createStatCard("Doctors on Duty", "14")
        );

        // --- Actionable Items ---
        VBox alertsBox = new VBox(10);
        Label alertsTitle = new Label("Actionable Items & Alerts");
        alertsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        alertsBox.getChildren().addAll(
                alertsTitle,
                new Label("• 2 Pending Doctor Approvals"),
                new Label("• 5 Unassigned Appointments for tomorrow")
        );

        // --- Quick Links ---
        HBox quickLinksBox = new HBox(15);
        Label quickLinksTitle = new Label("Quick Links");
        quickLinksTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button manageDoctorsBtn = new Button("Manage Doctors");
        Button managePatientsBtn = new Button("Manage Patients");
        Button scheduleBtn = new Button("Master Schedule");
        manageDoctorsBtn.getStyleClass().add("save-button");
        managePatientsBtn.getStyleClass().add("save-button");
        scheduleBtn.getStyleClass().add("save-button");
        quickLinksBox.getChildren().addAll(manageDoctorsBtn, managePatientsBtn, scheduleBtn);

        view.getChildren().addAll(title, kpiBox, alertsBox, new VBox(10, quickLinksTitle, quickLinksBox));
        return view;
    }

    // Reusing the stat card method from the Doctor's dashboard
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
