package lk.ijse.its1155_orm_course_work.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.its1155_orm_course_work.dto.tm.PatientHistoryTM;

import java.time.LocalDate;
import java.util.List;

public class PatientPopUpController {

    @FXML
    private Label lblName;
    @FXML
    private Label lblNic;
    @FXML
    private Label lblPhone;
    @FXML
    private Button btnPrint;
    @FXML
    private TableView<PatientHistoryTM> tblHistory;
    @FXML
    private TableColumn<PatientHistoryTM, LocalDate> colDate;
    @FXML
    private TableColumn<PatientHistoryTM, String> colProgram;
    @FXML
    private TableColumn<PatientHistoryTM, String> colTherapist;
    @FXML
    private TableColumn<PatientHistoryTM, String> colStatus;

    public void initialize() {

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    public void setPatientData(String name, String nic, String phone, List<PatientHistoryTM> history) {
        Platform.runLater(() -> {
            lblName.setText(name);
            lblNic.setText(nic);
            lblPhone.setText(phone);
            tblHistory.setItems(FXCollections.observableArrayList(history));
        });
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        // Jasper Report
        System.out.println("Printing report for: " + lblName.getText());
    }
}