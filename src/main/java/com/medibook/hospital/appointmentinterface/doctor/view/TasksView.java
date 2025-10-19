// In: doctor/view/TasksView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.TaskDAO;
import com.medibook.hospital.appointmentinterface.model.Task;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class TasksView {
    public Node getView(int doctorId) {
        VBox view = new VBox(20);
        Label title = new Label("My Tasks");
        title.getStyleClass().add("page-title");

        TableView<Task> table = new TableView<>();
        TableColumn<Task, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Task, LocalDate> dateCol = new TableColumn<>("Due Date");
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());

        TableColumn<Task, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(descCol, dateCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TaskDAO taskDAO = new TaskDAO();
        table.setItems(FXCollections.observableArrayList(taskDAO.getTasksForDoctor(doctorId)));

        view.getChildren().addAll(title, table);
        return view;
    }
}