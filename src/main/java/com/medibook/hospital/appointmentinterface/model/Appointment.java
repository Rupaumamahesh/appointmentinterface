// Create this new file: Appointment.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty doctor;
    private final StringProperty status;

    public Appointment(String date, String time, String doctor, String status) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.doctor = new SimpleStringProperty(doctor);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty doctorProperty() { return doctor; }
    public StringProperty statusProperty() { return status; }
}