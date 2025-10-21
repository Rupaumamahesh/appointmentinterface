// In: patient/view/MyAppointmentsView.java
package com.medibook.hospital.appointmentinterface.patient.view;

import com.medibook.hospital.appointmentinterface.dao.AppointmentDAO;
import com.medibook.hospital.appointmentinterface.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class MyAppointmentsView {

    private AppointmentDAO appointmentDAO;
    private ObservableList<Appointment> allAppointments;
    private int patientId;

    public Node getView(int patientId) {
        this.patientId = patientId;
        this.appointmentDAO = new AppointmentDAO();
        this.allAppointments = FXCollections.observableArrayList(appointmentDAO.getAppointmentsForPatient(patientId));

        VBox view = new VBox(20);
        Label title = new Label("My Appointments");
        title.getStyleClass().add("page-title");

        // --- Tabbed Navigation ---
        TabPane tabPane = new TabPane();

        Tab upcomingTab = new Tab("Upcoming Appointments", createTableView(true));
        Tab pastTab = new Tab("Past Appointments", createTableView(false));

        // Prevent tabs from being closed
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(upcomingTab, pastTab);

        view.getChildren().addAll(title, tabPane);
        return view;
    }

    private TableView<Appointment> createTableView(boolean isUpcoming) {
        TableView<Appointment> table = new TableView<>();

        // --- Define Columns ---
        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().appointmentDateProperty());

        TableColumn<Appointment, LocalTime> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().appointmentTimeProperty());

        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(cellData -> cellData.getValue().doctorNameProperty());

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // --- Filter Data for the Correct Tab ---
        if (isUpcoming) {
            List<Appointment> upcoming = allAppointments.stream()
                    .filter(a -> !a.getAppointmentDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());
            table.setItems(FXCollections.observableArrayList(upcoming));

            // --- Add Actions Column ONLY to Upcoming Tab ---
            TableColumn<Appointment, Void> actionCol = createActionColumn();
            table.getColumns().addAll(dateCol, timeCol, doctorCol, statusCol, actionCol);
        } else {
            List<Appointment> past = allAppointments.stream()
                    .filter(a -> a.getAppointmentDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());
            table.setItems(FXCollections.observableArrayList(past));
            table.getColumns().addAll(dateCol, timeCol, doctorCol, statusCol);
        }

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private TableColumn<Appointment, Void> createActionColumn() {
        TableColumn<Appointment, Void> actionCol = new TableColumn<>("Actions");

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button cancelBtn = new Button("Cancel");
            private final Button rescheduleBtn = new Button("Reschedule");
            private final HBox pane = new HBox(10, cancelBtn, rescheduleBtn);

            {
                pane.setAlignment(Pos.CENTER);
                cancelBtn.getStyleClass().add("action-button-secondary");
                rescheduleBtn.getStyleClass().add("action-button-secondary");

                cancelBtn.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel your appointment with " + appointment.getDoctorName() + "?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            boolean success = appointmentDAO.cancelAppointment(appointment.getId());
                            if (success) {
                                // Refresh the entire list
                                allAppointments.setAll(appointmentDAO.getAppointmentsForPatient(patientId));
                                getTableView().setItems(allAppointments.stream().filter(a -> !a.getAppointmentDate().isBefore(LocalDate.now())).collect(Collectors.toCollection(FXCollections::observableArrayList)));
                            } else {
                                // Show error alert
                            }
                        }
                    });
                });

                rescheduleBtn.setOnAction(event -> {
                    // Placeholder for a more complex reschedule workflow
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Reschedule");
                    info.setHeaderText("Feature In Development");
                    info.setContentText("This would launch the booking process again, pre-filled with the current doctor.");
                    info.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    // Only show buttons for appointments that are not already cancelled or completed
                    if (appointment.getStatus().equalsIgnoreCase("Pending") || appointment.getStatus().equalsIgnoreCase("Confirmed")) {
                        setGraphic(pane);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        return actionCol;
    }
}