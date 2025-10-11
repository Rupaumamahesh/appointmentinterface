// In: doctor/view/TasksView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TasksView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("My Tasks");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addTaskBtn = new Button("Add New Task");
        addTaskBtn.getStyleClass().add("save-button");
        header.getChildren().addAll(title, spacer, addTaskBtn);

        TableView<Task> table = new TableView<>();
        TableColumn<Task, String> descCol = new TableColumn<>("Task Description");
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        TableColumn<Task, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        TableColumn<Task, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());

        table.getColumns().addAll(descCol, priorityCol, dueCol);
        table.setItems(getDummyTasks());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(header, table);
        return view;
    }

    private ObservableList<Task> getDummyTasks() {
        return FXCollections.observableArrayList(
                new Task("Follow up on John Doe's lab results", "High", "Today"),
                new Task("Sign referral form for Jane Smith", "Medium", "Tomorrow"),
                new Task("Review chart from Dr. Wang", "Low", "End of Week")
        );
    }
}
