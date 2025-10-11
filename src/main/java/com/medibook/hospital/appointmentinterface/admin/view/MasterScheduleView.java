// In: admin/view/MasterScheduleView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import com.medibook.hospital.appointmentinterface.model.Appointment;
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

public class MasterScheduleView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Master Schedule");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button bookAppointmentBtn = new Button("Book New Appointment");
        bookAppointmentBtn.getStyleClass().add("save-button");
        header.getChildren().addAll(title, spacer, bookAppointmentBtn);

        // A full master calendar is complex. A table is a great starting point.
        TableView<Appointment> table = new TableView<>();
        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        // We'll reuse doctorProperty for patient and statusProperty for doctor
        patientCol.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(dateCol, timeCol, patientCol, doctorCol);
        table.setItems(getDummyAppointments());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(header, table);
        return view;
    }

    private ObservableList<Appointment> getDummyAppointments() {
        // Reusing the Appointment model.
        // For this table, "doctor" field holds the patient, "status" field holds the doctor.
        return FXCollections.observableArrayList(
                new Appointment("Today", "09:00 AM", "John Doe", "Dr. Smith"),
                new Appointment("Today", "09:30 AM", "Jane Smith", "Dr. Smith"),
                new Appointment("Today", "10:00 AM", "Sam Wilson", "Dr. Jones")
        );
    }
}
