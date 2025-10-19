// In: doctor/view/DoctorScheduleView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.AppointmentDAO;
import com.medibook.hospital.appointmentinterface.model.Appointment;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorScheduleView {

    public Node getView(int doctorId) {
        VBox view = new VBox(20);
        Label title = new Label("My Schedule");
        title.getStyleClass().add("page-title");

        // --- Create the Table ---
        TableView<Appointment> table = new TableView<>();

        // This view doesn't need the doctor's name, so we only show the patient
        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().appointmentDateProperty());

        TableColumn<Appointment, LocalTime> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeProperty());

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(dateCol, timeCol, patientCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- Load Data Dynamically from the DAO ---
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        table.setItems(FXCollections.observableArrayList(appointmentDAO.getAppointmentsForDoctor(doctorId)));

        view.getChildren().addAll(title, table);
        return view;
    }
}