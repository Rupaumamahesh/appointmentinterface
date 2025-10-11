// In: doctor/view/DoctorProfileView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DoctorProfileView {
    public Node getView() {
        VBox view = new VBox(30);
        view.setPadding(new Insets(25));

        Label title = new Label("My Profile & Settings");
        title.getStyleClass().add("page-title");

        // --- Personal Information Section ---
        VBox personalInfoBox = new VBox(10);
        Label personalInfoTitle = new Label("Personal Information");
        personalInfoTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.add(new Label("Full Name:"), 0, 0);
        formGrid.add(new TextField("Dr. John Smith"), 1, 0);
        formGrid.add(new Label("Specialty:"), 0, 1);
        formGrid.add(new TextField("Cardiology"), 1, 1);
        personalInfoBox.getChildren().addAll(personalInfoTitle, formGrid);

        // --- Security Section ---
        VBox securityBox = new VBox(10);
        Label securityTitle = new Label("Security");
        securityTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.getStyleClass().add("save-button");
        securityBox.getChildren().addAll(securityTitle, changePasswordBtn);

        Button saveButton = new Button("Save All Changes");
        saveButton.getStyleClass().add("save-button");

        view.getChildren().addAll(title, personalInfoBox, securityBox, saveButton);
        return view;
    }
}
