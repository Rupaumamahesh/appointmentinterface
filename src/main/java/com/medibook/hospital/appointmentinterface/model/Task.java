// In: model/Task.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private final StringProperty description;
    private final StringProperty priority;
    private final StringProperty dueDate;

    public Task(String description, String priority, String dueDate) {
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleStringProperty(priority);
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public StringProperty descriptionProperty() { return description; }
    public StringProperty priorityProperty() { return priority; }
    public StringProperty dueDateProperty() { return dueDate; }
}
