package lk.ijse.its1155_orm_course_work.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapyScheduleTM;

import java.util.List;

public class TherapistSchedulePopUpController {
    @FXML
    private Label lblDocName;
    @FXML
    private Label lblSpecialization;
    @FXML
    private Label lblTotalSessions;
    @FXML
    private TableView<TherapyScheduleTM> tblDoctorSchedule;
    @FXML
    private TableColumn<TherapyScheduleTM, String> colTime;
    @FXML
    private TableColumn<TherapyScheduleTM, String> colPatient;
    @FXML
    private TableColumn<TherapyScheduleTM, String> colStatus;

    public void initialize() {

        colTime.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void setDoctorDetails(String name, String specialization, List<TherapyScheduleTM> sessionList) {
        Platform.runLater(() -> {
            lblDocName.setText("Dr. " + name);
            lblSpecialization.setText(specialization);
            lblTotalSessions.setText(String.format("%02d", sessionList.size()));
            tblDoctorSchedule.setItems(FXCollections.observableArrayList(sessionList));
        });
    }
}
