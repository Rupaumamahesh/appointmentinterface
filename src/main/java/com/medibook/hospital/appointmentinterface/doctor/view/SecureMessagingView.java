// In: doctor/view/SecureMessagingView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.MessageDAO;
import com.medibook.hospital.appointmentinterface.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.util.List;

public class SecureMessagingView {

    private TableView<Message> table;
    private MessageDAO messageDAO;

    public SecureMessagingView() {
        this.messageDAO = new MessageDAO();
    }

    public Node getView(int doctorId) {
        BorderPane view = new BorderPane();

        // --- Header ---
        HBox header = createHeader();
        view.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 20, 0));

        // --- Message Table ---
        this.table = createMessageTable();
        view.setCenter(table);

        // --- Load the dynamic data ---
        loadMessages(doctorId);

        return view;
    }

    // This method now populates the table with real data
    private void loadMessages(int doctorId) {
        // 1. Fetch data from the database using the DAO
        List<Message> messageList = messageDAO.getMessagesForDoctor(doctorId);

        // 2. Convert the standard List to a JavaFX ObservableList
        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messageList);

        // 3. Set the data on the table to display it
        table.setItems(observableMessages);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Secure Messaging");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button composeBtn = new Button("Compose New Message");
        composeBtn.getStyleClass().add("action-button");

        // Add action for the compose button later
        composeBtn.setOnAction(e -> handleComposeMessage());

        header.getChildren().addAll(title, spacer, composeBtn);
        return header;
    }

    private TableView<Message> createMessageTable() {
        TableView<Message> tv = new TableView<>();
        TableColumn<Message, String> fromCol = new TableColumn<>("From");
        fromCol.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
        fromCol.setPrefWidth(200);

        TableColumn<Message, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        subjectCol.setPrefWidth(500);

        TableColumn<Message, String> timeCol = new TableColumn<>("Received");
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
        timeCol.setPrefWidth(200);

        tv.getColumns().addAll(fromCol, subjectCol, timeCol);

        // Add action for clicking on a row
        tv.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tv.getSelectionModel().getSelectedItem() != null) {
                handleReadMessage(tv.getSelectionModel().getSelectedItem());
            }
        });

        return tv;
    }

    // Placeholder methods for future functionality
    private void handleComposeMessage() {
        // This is a placeholder action. In a real app, this would open a new dialog
        // window for composing a message. For now, it just shows a confirmation.
        System.out.println("Compose new message clicked!");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Compose Message");
        alert.setHeaderText("Feature Not Implemented");
        alert.setContentText("This would open a new window to write and send a message.");
        alert.showAndWait();
    }

    private void handleReadMessage(Message message) {
        // This would open a new window showing the full message content.
        System.out.println("Reading message from: " + message.getFrom() + " | Subject: " + message.getSubject());
        // Example: new ReadMessageDialog(message).showAndWait();
    }
}