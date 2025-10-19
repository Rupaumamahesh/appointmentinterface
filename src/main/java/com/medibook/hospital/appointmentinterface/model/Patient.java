// In: model/Patient.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Patient {
    private final IntegerProperty id;
    private final StringProperty fullName;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty gender;

    public Patient(int id, String fullName, LocalDate dateOfBirth, String gender) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.gender = new SimpleStringProperty(gender);
    }

    // --- START: ADD/REPLACE THIS ENTIRE SECTION ---

    // --- Standard Getters (for regular Java code) ---
    public int getId() { return id.get(); }
    public String getFullName() { return fullName.get(); }
    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public String getGender() { return gender.get(); }

    // --- JavaFX Property Getters (for TableView) ---
    public IntegerProperty idProperty() { return id; }
    public StringProperty fullNameProperty() { return fullName; }
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty genderProperty() { return gender; }

    // --- END: ADD/REPLACE THIS ENTIRE SECTION ---
}