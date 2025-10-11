// In: model/Doctor.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Doctor {
    private final StringProperty name;
    private final StringProperty specialty;
    private final StringProperty status;
    private final StringProperty email;

    public Doctor(String name, String specialty, String status, String email) {
        this.name = new SimpleStringProperty(name);
        this.specialty = new SimpleStringProperty(specialty);
        this.status = new SimpleStringProperty(status);
        this.email = new SimpleStringProperty(email);
    }

    // JavaFX property methods
    public StringProperty nameProperty() { return name; }
    public StringProperty specialtyProperty() { return specialty; }
    public StringProperty statusProperty() { return status; }
    public StringProperty emailProperty() { return email; }
}