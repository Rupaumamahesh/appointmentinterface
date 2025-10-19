// In: model/Doctor.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.*;

public class Doctor {
    private final IntegerProperty id;
    private final StringProperty fullName;
    private final StringProperty specialization;
    private final StringProperty email;
    private final StringProperty status;

    public Doctor(int id, String fullName, String specialization, String email, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.specialization = new SimpleStringProperty(specialization);
        this.email = new SimpleStringProperty(email);
        this.status = new SimpleStringProperty(status);
    }

    // --- Standard Getters ---
    public int getId() { return id.get(); }
    public String getFullName() { return fullName.get(); }
    public String getSpecialization() { return specialization.get(); }
    public String getEmail() { return email.get(); }
    public String getStatus() { return status.get(); }

    // --- JavaFX Property Getters ---
    public IntegerProperty idProperty() { return id; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty specializationProperty() { return specialization; }
    public StringProperty emailProperty() { return email; }
    public StringProperty statusProperty() { return status; }
}