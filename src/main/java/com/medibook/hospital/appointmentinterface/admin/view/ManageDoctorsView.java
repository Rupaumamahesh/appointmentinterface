// In: admin/view/ManageDoctorsView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import com.medibook.hospital.appointmentinterface.dao.DoctorDAO;
import com.medibook.hospital.appointmentinterface.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class ManageDoctorsView {

    private TableView<Doctor> table;
    private DoctorDAO doctorDAO;

    public Node getView() {
        this.doctorDAO = new DoctorDAO();

        VBox view = new VBox(20);
        Label title = new Label("Manage Doctors");
        title.getStyleClass().add("page-title");

        table = new TableView<>();

        // --- FIX 1: Use the new property names ---
        TableColumn<Doctor, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());

        TableColumn<Doctor, String> specialtyCol = new TableColumn<>("Specialization");
        specialtyCol.setCellValueFactory(cellData -> cellData.getValue().specializationProperty());

        TableColumn<Doctor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        TableColumn<Doctor, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(nameCol, specialtyCol, emailCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- FIX 2: Use the new DAO method to load all doctors ---
        loadAllDoctors();

        view.getChildren().addAll(title, table);
        return view;
    }

    private void loadAllDoctors() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList(doctorDAO.getAllDoctors());
        table.setItems(doctors);
    }
}
