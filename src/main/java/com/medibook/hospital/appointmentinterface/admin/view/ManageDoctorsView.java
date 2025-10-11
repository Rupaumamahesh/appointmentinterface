// In: admin/view/ManageDoctorsView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import com.medibook.hospital.appointmentinterface.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

public class ManageDoctorsView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Manage Doctors");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addDoctorBtn = new Button("Add New Doctor");
        addDoctorBtn.getStyleClass().add("save-button");
        header.getChildren().addAll(title, spacer, addDoctorBtn);

        // Table of doctors
        TableView<Doctor> table = new TableView<>();
        TableColumn<Doctor, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        TableColumn<Doctor, String> specialtyCol = new TableColumn<>("Specialty");
        specialtyCol.setCellValueFactory(cellData -> cellData.getValue().specialtyProperty());
        TableColumn<Doctor, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        TableColumn<Doctor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        table.getColumns().addAll(nameCol, specialtyCol, statusCol, emailCol);
        table.setItems(getDummyDoctors());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(header, table);
        return view;
    }

    private ObservableList<Doctor> getDummyDoctors() {
        return FXCollections.observableArrayList(
                new Doctor("Dr. John Smith", "Cardiology", "Active", "j.smith@medibook.com"),
                new Doctor("Dr. Emily Jones", "Pediatrics", "Active", "e.jones@medibook.com"),
                new Doctor("Dr. Chen Wang", "Neurology", "Pending Approval", "c.wang@medibook.com")
        );
    }
}
