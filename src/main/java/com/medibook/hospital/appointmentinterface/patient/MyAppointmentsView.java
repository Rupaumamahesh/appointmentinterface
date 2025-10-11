// Create this new file: MyAppointmentsView.java
package com.medibook.hospital.appointmentinterface.patient;

import com.medibook.hospital.appointmentinterface.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class MyAppointmentsView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("content-area");

        Label title = new Label("My Appointments");
        title.getStyleClass().add("page-title");

        TableView<Appointment> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(dateCol, timeCol, doctorCol, statusCol);
        table.setItems(getDummyAppointments());

        view.getChildren().addAll(title, table);
        return view;
    }

    private ObservableList<Appointment> getDummyAppointments() {
        return FXCollections.observableArrayList(
                new Appointment("2025-10-28", "10:00 AM", "Dr. Smith", "Confirmed"),
                new Appointment("2025-11-15", "02:30 PM", "Dr. Jones", "Pending")
        );
    }
}
