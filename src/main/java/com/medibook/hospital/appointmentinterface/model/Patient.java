// In: model/Patient.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final StringProperty name;
    private final StringProperty dob;
    private final StringProperty gender;
    private final StringProperty lastVisit;

    public Patient(String name, String dob, String gender, String lastVisit) {
        this.name = new SimpleStringProperty(name);
        this.dob = new SimpleStringProperty(dob);
        this.gender = new SimpleStringProperty(gender);
        this.lastVisit = new SimpleStringProperty(lastVisit);
    }

    // JavaFX property methods
    public StringProperty nameProperty() { return name; }
    public StringProperty dobProperty() { return dob; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty lastVisitProperty() { return lastVisit; }
}