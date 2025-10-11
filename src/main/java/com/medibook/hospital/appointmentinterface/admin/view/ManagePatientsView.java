// In: admin/view/ManagePatientsView.java
package com.medibook.hospital.appointmentinterface.admin.view;

import com.medibook.hospital.appointmentinterface.model.Patient; // Reusing the patient model
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

public class ManagePatientsView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Manage Patients");
        title.getStyleClass().add("page-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addPatientBtn = new Button("Register New Patient");
        addPatientBtn.getStyleClass().add("save-button");
        header.getChildren().addAll(title, spacer, addPatientBtn);

        TableView<Patient> table = new TableView<>();
        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        TableColumn<Patient, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(cellData -> cellData.getValue().dobProperty());
        TableColumn<Patient, String> lastVisitCol = new TableColumn<>("Last Visit");
        lastVisitCol.setCellValueFactory(cellData -> cellData.getValue().lastVisitProperty());

        table.getColumns().addAll(nameCol, dobCol, lastVisitCol);
        table.setItems(getDummyPatients());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        view.getChildren().addAll(header, table);
        return view;
    }

    private ObservableList<Patient> getDummyPatients() {
        return FXCollections.observableArrayList(
                new Patient("John Doe", "1985-05-20", "Male", "2025-10-10"),
                new Patient("Jane Smith", "1992-11-30", "Female", "2025-09-15"),
                new Patient("Peter Jones", "1978-01-12", "Male", "2025-10-28")
        );
    }
}
