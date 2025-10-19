// In: model/Appointment.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> appointmentDate;
    private final ObjectProperty<LocalTime> appointmentTime;
    private final StringProperty patientName;
    private final StringProperty doctorName;
    private final StringProperty status;

    public Appointment(int id, LocalDate appointmentDate, LocalTime appointmentTime, String patientName, String doctorName, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.appointmentDate = new SimpleObjectProperty<>(appointmentDate);
        this.appointmentTime = new SimpleObjectProperty<>(appointmentTime);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.status = new SimpleStringProperty(status);
    }

    // --- JavaFX Property Getters (These are what the TableView uses) ---
    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<LocalDate> appointmentDateProperty() { return appointmentDate; }
    public ObjectProperty<LocalTime> appointmentTimeProperty() { return appointmentTime; }
    public StringProperty patientNameProperty() { return patientName; }
    public StringProperty doctorNameProperty() { return doctorName; }
    public StringProperty statusProperty() { return status; }
}