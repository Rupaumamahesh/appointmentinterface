// In: doctor/view/AvailabilityView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AvailabilityView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        Label title = new Label("Manage Availability");
        title.getStyleClass().add("page-title");

        Label placeholder = new Label("A full calendar component for setting working hours, blocking time off, and defining appointment slot durations would go here.");
        placeholder.setWrapText(true);

        view.getChildren().addAll(title, placeholder);
        return view;
    }
}