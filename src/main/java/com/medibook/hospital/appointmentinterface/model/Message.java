// In: model/Message.java
package com.medibook.hospital.appointmentinterface.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
    private final StringProperty from;
    private final StringProperty subject;
    private final StringProperty timestamp;

    public Message(String from, String subject, String timestamp) {
        this.from = new SimpleStringProperty(from);
        this.subject = new SimpleStringProperty(subject);
        this.timestamp = new SimpleStringProperty(timestamp);
    }

    // --- Property Getters (for JavaFX TableView) ---
    // These return the entire "Property" object, which lets the table bind to the data.
    public StringProperty fromProperty() {
        return from;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    // --- START: ADD THIS SECTION ---
    // --- Standard Getters (for regular Java code) ---
    // These return the actual String value inside the property.
    public String getFrom() {
        return from.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getTimestamp() {
        return timestamp.get();
    }
    // --- END: ADD THIS SECTION ---
}