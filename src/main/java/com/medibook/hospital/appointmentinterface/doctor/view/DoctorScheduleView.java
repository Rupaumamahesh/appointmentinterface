// In: doctor/view/DoctorScheduleView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class DoctorScheduleView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        Label title = new Label("Full Schedule");
        title.getStyleClass().add("page-title");
        // Note: A full interactive calendar is an advanced component.
        // A TableView is an excellent and practical V1.

        TableView<Appointment> table = new TableView<>();
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cellData -> cellData.getValue().doctorProperty()); // Reusing for patient name

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(timeCol, patientCol, statusCol);
        table.setItems(getDummyAppointments());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(title, table);
        return view;
    }

    private ObservableList<Appointment> getDummyAppointments() {
        return FXCollections.observableArrayList(
                new Appointment("Today", "09:00 AM", "John Doe", "Confirmed"),
                new Appointment("Today", "09:30 AM", "Jane Smith", "Confirmed"),
                new Appointment("Today", "10:15 AM", "Peter Jones", "Checked In"),
                new Appointment("Today", "11:00 AM", "Mary Williams", "Pending"),
                new Appointment("Tomorrow", "01:30 PM", "David Brown", "Confirmed")
        );
    }
}