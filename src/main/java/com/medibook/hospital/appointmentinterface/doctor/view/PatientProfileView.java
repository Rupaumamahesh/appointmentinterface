// In: doctor/view/PatientProfileView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PatientProfileView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        // --- Patient Header ---
        Label patientName = new Label("John Doe, 40");
        patientName.getStyleClass().add("page-title");
        Label allergies = new Label("Allergies: Penicillin");
        allergies.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        VBox header = new VBox(5, patientName, allergies);

        // --- Tabbed Interface for EHR ---
        TabPane tabPane = new TabPane();
        Tab visitHistoryTab = new Tab("Visit History", new Label("Content for Visit History"));
        Tab clinicalNotesTab = new Tab("Clinical Notes", new Label("Content for Clinical Notes"));
        Tab prescriptionsTab = new Tab("Prescriptions", new Label("Content for Prescriptions"));
        Tab labsTab = new Tab("Labs & Documents", new Label("Content for Labs & Documents"));

        tabPane.getTabs().addAll(visitHistoryTab, clinicalNotesTab, prescriptionsTab, labsTab);

        view.getChildren().addAll(header, tabPane);
        return view;
    }
}
