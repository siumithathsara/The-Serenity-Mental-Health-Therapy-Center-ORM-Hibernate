package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.PatientDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.PatientService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapistService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapyProgramingService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TherapySessionController implements Initializable {

    private final PatientService patientService = (PatientService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.PATIENT);
    private final TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPIST);
    private final TherapyProgramingService programService = (TherapyProgramingService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_PROGRAM);


    @FXML
    private Button btnBook;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnReschedule;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private ComboBox<String> cmbStatus1;

    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private ComboBox<String> cmbTimeSlot;

    @FXML
    private TableColumn<?, ?> colDateTime;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colPatient;

    @FXML
    private TableColumn<?, ?> colProgram;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTherapist;

    @FXML
    private DatePicker dpFilterDate;

    @FXML
    private DatePicker dpSessionDate;

    @FXML
    private AnchorPane schedulingPage;

    @FXML
    private TableView<?> tblAppointments;

    @FXML
    private TextField txtAppointmentId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtSearch;

    private final ObservableList<String> statusList = FXCollections.observableArrayList("PENDING", "CONFIRMED");
    private final ObservableList<String> timeSlots = FXCollections.observableArrayList(
            "08:00 AM - 09:00 AM", "09:00 AM - 10:00 AM", "10:00 AM - 11:00 AM", "01:00 PM - 02:00 PM"
    );
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadPatientDetails();
        loadProgramDetails();
    }

    private void loadPatientDetails() {
        try {
            List<PatientDTO> patientDTOS = patientService.getPatients();

            ObservableList<String> obList = FXCollections.observableArrayList();

            for (PatientDTO patientDTO : patientDTOS) {
                obList.add(String.valueOf(patientDTO.getId()));
            }

            cmbStatus1.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void handleSelectPatientId(ActionEvent event) {

        try {
            String selectPatientId =
                    cmbStatus1.getSelectionModel().getSelectedItem();

            if (selectPatientId == null || selectPatientId.isBlank()) {
                txtPatientName.setText("");
                return;
            }

            PatientDTO patientDTO =
                    patientService.searchPatient(selectPatientId);

            if (patientDTO == null) {
                txtPatientName.setText("");
                return;
            }

            txtPatientName.setText(patientDTO.getName());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Failed to load patient details").show();
        }
    }

    private void loadProgramDetails() {
        try {
            List<TherapyProgramDTO> programDTOS = programService.getPrograms();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (TherapyProgramDTO programDTO : programDTOS) {

                obList.add(programDTO.getId() + " - " + programDTO.getProgramName());
            }
            cmbProgram.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load program details").show();
        }
    }

    @FXML
    void handleSelectProgram(ActionEvent event) {
        try {
            String selectedProgram = cmbProgram.getSelectionModel().getSelectedItem();

            cmbTherapist.getItems().clear();

            if (selectedProgram == null || selectedProgram.isBlank()) {
                return;
            }

            String programName = selectedProgram.split(" - ")[1].trim();

            List<TherapistDTO> therapistDTOS = therapistService.getAllTherapists(programName);

            if (therapistDTOS == null || therapistDTOS.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No active therapists assigned for this program!").show();
                return;
            }

            ObservableList<String> obList = FXCollections.observableArrayList();
            for (TherapistDTO therapistDTO : therapistDTOS) {
                obList.add(therapistDTO.getId() + " - " + "Dr. " + therapistDTO.getName());
            }

            cmbTherapist.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Can't load assigned therapists!").show();
        }
    }


}
