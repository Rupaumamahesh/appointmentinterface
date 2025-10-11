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

    public StringProperty fromProperty() { return from; }
    public StringProperty subjectProperty() { return subject; }
    public StringProperty timestampProperty() { return timestamp; }
}
