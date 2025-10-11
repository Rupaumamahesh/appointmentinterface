// Create this new file: MyProfileView.java
package com.medibook.hospital.appointmentinterface.patient;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MyProfileView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("content-area");

        Label title = new Label("My Profile");
        title.getStyleClass().add("page-title");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        formGrid.add(new Label("Full Name:"), 0, 0);
        formGrid.add(new TextField("Rupa P."), 1, 0);

        formGrid.add(new Label("Email:"), 0, 1);
        formGrid.add(new TextField("rupa.p@example.com"), 1, 1);

        Button saveButton = new Button("Save Changes");
        saveButton.getStyleClass().add("save-button");

        view.getChildren().addAll(title, formGrid, saveButton);
        return view;
    }
}
