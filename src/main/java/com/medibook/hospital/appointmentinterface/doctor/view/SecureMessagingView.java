// In: doctor/view/SecureMessagingView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SecureMessagingView {
    public Node getView() {
        BorderPane view = new BorderPane();
        view.setPadding(new Insets(25));

        // --- Header ---
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Secure Messaging");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button composeBtn = new Button("Compose New Message");
        composeBtn.getStyleClass().add("save-button");
        header.getChildren().addAll(title, spacer, composeBtn);
        view.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 20, 0));

        // --- Message List (like an inbox) ---
        TableView<Message> table = new TableView<>();
        TableColumn<Message, String> fromCol = new TableColumn<>("From");
        fromCol.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
        TableColumn<Message, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        TableColumn<Message, String> timeCol = new TableColumn<>("Received");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());

        table.getColumns().addAll(fromCol, subjectCol, timeCol);
        table.setItems(getDummyMessages());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        view.setCenter(table);

        return view;
    }

    private ObservableList<Message> getDummyMessages() {
        return FXCollections.observableArrayList(
                new Message("Admin Staff", "New Patient Assigned: John Doe", "Today, 10:45 AM"),
                new Message("Dr. Emily Jones", "RE: Consultation Request", "Today, 9:15 AM"),
                new Message("LabCorp", "Lab Results for Jane Smith", "Yesterday, 3:30 PM")
        );
    }
}