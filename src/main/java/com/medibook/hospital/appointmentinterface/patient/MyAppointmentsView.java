// In: patient/MyAppointmentsView.java
package com.medibook.hospital.appointmentinterface.patient;

import com.medibook.hospital.appointmentinterface.dao.AppointmentDAO;
import com.medibook.hospital.appointmentinterface.model.Appointment;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MyAppointmentsView {

    private TableView<Appointment> table;
    private AppointmentDAO appointmentDAO;

    public Node getView() {
        this.appointmentDAO = new AppointmentDAO();

        VBox view = new VBox(20);
        view.setPadding(new Insets(10));

        Label title = new Label("My Appointments");
        title.getStyleClass().add("page-title");

        table = new TableView<>();

        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        TableColumn<Appointment, LocalTime> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(dateCol, timeCol, doctorCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(title, table);
        return view;
    }

    public void loadAppointments(int patientId) {
        List<Appointment> appointments = appointmentDAO.getAppointmentsForPatient(patientId);
        table.setItems(FXCollections.observableArrayList(appointments));
    }
}