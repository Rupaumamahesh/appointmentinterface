// In: patient/view/PatientDashboardView.java
package com.medibook.hospital.appointmentinterface.patient.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PatientDashboardView {
    public Node getView() {
        VBox layout = new VBox();
        Label title = new Label("Patient Dashboard (Under Construction)");
        title.getStyleClass().add("page-title");
        layout.getChildren().add(title);
        return layout;
    }
}