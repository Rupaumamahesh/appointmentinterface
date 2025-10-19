// In: model/Task.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Task {
    private final StringProperty description;
    private final ObjectProperty<LocalDate> dueDate;
    private final StringProperty status;

    public Task(String description, LocalDate dueDate, String status) {
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.status = new SimpleStringProperty(status);
    }

    // Property Getters for TableView
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public StringProperty statusProperty() { return status; }
}