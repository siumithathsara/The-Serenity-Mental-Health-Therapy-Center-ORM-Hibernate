package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lk.ijse.its1155_orm_course_work.dto.PatientDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapySessionDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapyScheduleTM;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.PatientService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapistService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapyProgramingService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapySessionService;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapySessionController implements Initializable {

    private final PatientService patientService = (PatientService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.PATIENT);
    private final TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPIST);
    private final TherapyProgramingService programService = (TherapyProgramingService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_PROGRAM);
    private final TherapySessionService sessionService = (TherapySessionService)  ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_SESSION);

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
    private TableView<TherapySessionDTO> tblAppointments;

    @FXML
    private TextField txtAppointmentId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtSearch;

    private final ObservableList<String> statusList = FXCollections.observableArrayList("PENDING", "CONFIRMED");
    private final ObservableList<String> timeSlots = FXCollections.observableArrayList(
               "        08:00 AM - 09:00 AM" ,
                    "        09:00 AM - 10:00 AM" ,
                    "        10:00 AM - 11:00 AM" ,
                    "        11:00 AM - 12:00 PM" ,
                    "        01:00 PM - 02:00 PM" ,
                    "        02:00 PM - 03:00 PM" ,
                    "        03:00 PM - 04:00 PM" ,
                    "        04:00 PM - 05:00 PM" ,
                    "        05:00 PM - 06:00 PM" ,
                    "        06:00 PM - 07:00 PM"
    );
    private List<TherapistDTO> currentTherapistList = new ArrayList<>();
    private List<String> bookedSlotsList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadPatientDetails();
        loadProgramDetails();
        generateNewId();
        loadSessionTable();
        cmbStatus.setItems(statusList);
        cmbTimeSlot.setItems(timeSlots);

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
            this.currentTherapistList = therapistDTOS;

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

    private void setAvailableDatesForTherapist(TherapistDTO selectedTherapist) {
        if (selectedTherapist == null || selectedTherapist.getWorkingDays() == null || selectedTherapist.getWorkingDays().isEmpty()) {
            dpSessionDate.setDayCellFactory(null);
            return;
        }

        Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #eeeeee;");
                        }
                        else {
                            String fullDayName = item.getDayOfWeek().toString().toLowerCase();
                            String shortDayName = fullDayName.substring(0, 3);

                            boolean isWorkingDay = false;
                            for (Object dayObj : selectedTherapist.getWorkingDays()) {
                                String day = dayObj.toString().toLowerCase();
                                if (day.contains(fullDayName) || day.contains(shortDayName)) {
                                    isWorkingDay = true;
                                    break;
                                }
                            }

                            if (!isWorkingDay) {
                                setDisable(true);
                                setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #999999;");
                            } else {
                                setDisable(false);
                                setStyle("");
                            }
                        }
                    }
                };
            }
        };

        dpSessionDate.setDayCellFactory(dayCellFactory);
    }

    @FXML
    void handleTherapistDate(ActionEvent event) {
        String selectedItem = cmbTherapist.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        String therapistId = selectedItem.split(" - ")[0].trim();

        TherapistDTO selectedTherapistDTO = null;
        for (TherapistDTO dto : currentTherapistList) {
            if (dto.getId().equals(therapistId)) {
                selectedTherapistDTO = dto;
                break;
            }
        }

        setAvailableDatesForTherapist(selectedTherapistDTO);
    }

    @FXML
    void handleTimeSlot(ActionEvent event) {
        String selectedTherapist = cmbTherapist.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = dpSessionDate.getValue();

        if (selectedTherapist == null || selectedDate == null) return;

        String therapistId = selectedTherapist.split(" - ")[0].trim();


        try {
            bookedSlotsList = sessionService.getBookedTimeSlots(therapistId, selectedDate);


            cmbTimeSlot.setItems(FXCollections.observableArrayList(timeSlots));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateNewId(){
        try {
            String nextId = sessionService.generateNextCustomerId();
            txtAppointmentId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Patient ID").show();
        }
    }

    @FXML
    void handleBookingSessionSave(ActionEvent event) {

        String appointmentId = txtAppointmentId.getText().trim();
        String selectedPatientId = cmbStatus1.getSelectionModel().getSelectedItem();
        String patientName = txtPatientName.getText().trim();
        String selectedProgram = cmbProgram.getSelectionModel().getSelectedItem();
        String selectedTherapist = cmbTherapist.getSelectionModel().getSelectedItem();
        LocalDate sessionDate = dpSessionDate.getValue();
        String timeSlot = cmbTimeSlot.getSelectionModel().getSelectedItem();
        String status = cmbStatus.getSelectionModel().getSelectedItem();




        if (appointmentId.isEmpty() ||
                selectedPatientId == null || selectedPatientId.isEmpty() ||
                patientName.isEmpty() ||
                selectedProgram == null ||
                selectedTherapist == null ||
                sessionDate == null ||
                timeSlot == null || timeSlot.isEmpty() ||
                status == null || status.isEmpty()) {
            System.out.println("ID: " + appointmentId + ", PatientID: " + selectedPatientId +
                    ", Name: " + patientName + ", Program: " + selectedProgram +
                    ", Therapist: " + selectedTherapist + ", Date: " + sessionDate +
                    ", Time: " + timeSlot + ", Status: " + status);
            new Alert(Alert.AlertType.WARNING, "Please fill all fields before booking!").show();
            return;

        }

        if (!appointmentId.matches("^[A-Za-z0-9]{3,10}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Appointment ID format!").show();
            txtAppointmentId.requestFocus();
            return;
        }

        if (!patientName.matches("^[A-Za-z ]+$")) {
            new Alert(Alert.AlertType.ERROR, "Patient Name can only contain letters and spaces!").show();
            txtPatientName.requestFocus();
            return;
        }

        try {

            String programId = selectedProgram.split(" - ")[0].trim();
            String therapistId = selectedTherapist.split(" - ")[0].trim();
            String cleanTimeSlot = timeSlot.trim();

            boolean isAlreadyBooked = sessionService.isSlotBooked(therapistId, sessionDate, cleanTimeSlot);

            if (isAlreadyBooked) {
                new Alert(Alert.AlertType.ERROR, "Selected Therapist is already booked for this Time Slot!").show();
                return;
            }
            TherapySessionDTO sessionDTO = new TherapySessionDTO(
                    appointmentId,
                    status,
                    sessionDate,
                    cleanTimeSlot,
                    selectedPatientId,
                    programId,
                    therapistId
            );


            boolean isSaved = sessionService.bookSession(sessionDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Therapy Session Booked Successfully").show();

                if ("Confirmed".equalsIgnoreCase(status)) {
                    openPaymentUI(appointmentId);
                }
                clearFields();
                generateNewId();
                loadSessionTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to book the session").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
        }
    }

    private void clearFields() {
        cmbStatus1.getSelectionModel().clearSelection();
        txtPatientName.clear();
        cmbProgram.getSelectionModel().clearSelection();
        cmbTherapist.getSelectionModel().clearSelection();
        dpSessionDate.setValue(null);
        cmbTimeSlot.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
    }

    private void loadSessionTable(){
        try {

            List<TherapySessionDTO> sessionList = sessionService.getAllSession();

            ObservableList<TherapySessionDTO> obList = FXCollections.observableArrayList(sessionList);

            tblAppointments.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading data!").show();
        }
    }

    private void openPaymentUI(String appointmentId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paymentPage.fxml"));
            Parent root = loader.load();


            PaymentController controller = loader.getController();
            controller.setAppointmentId(appointmentId);

            Stage stage = new Stage();
            stage.setTitle("Payment Processing");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not open Payment UI!").show();
        }
    }

    @FXML
    void handleSessionSearch(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {
                String appointmentId = txtAppointmentId.getText().trim();

                if (appointmentId.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Please enter an Appointment ID!").show();
                    return;
                }

                TherapySessionDTO sessionDTO = sessionService.searchSession(appointmentId);

                if (sessionDTO != null) {

                    cmbStatus1.getSelectionModel().select(sessionDTO.getPatientId());
                    txtPatientName.setText(sessionDTO.getPatientName());
                    cmbProgram.getSelectionModel().select(sessionDTO.getProgramId() + " - " + sessionDTO.getProgramName());
                    cmbTherapist.getSelectionModel().select(sessionDTO.getTherapistId() + " - " + sessionDTO.getTherapistName());
                    dpSessionDate.setValue(sessionDTO.getSessionDate());
                    cmbTimeSlot.getSelectionModel().select(sessionDTO.getTimeSlot());
                    cmbStatus.getSelectionModel().select(sessionDTO.getStatus());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Session not found!").show();
                    clearFields();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void handleSessionUpdate(ActionEvent event) {
        try {
            String apptId = txtAppointmentId.getText().trim();
            String selectedTherapist = cmbTherapist.getSelectionModel().getSelectedItem();
            String therapistId = selectedTherapist.split(" - ")[0].trim();
            LocalDate date = dpSessionDate.getValue();
            String time = cmbTimeSlot.getSelectionModel().getSelectedItem();


            if (sessionService.isSlotBookedForUpdate(therapistId, date, time, apptId)) {
                new Alert(Alert.AlertType.ERROR, "Selected therapist is already booked for this Time Slot!").show();
                return;
            }

            TherapySessionDTO dto = new TherapySessionDTO(
                    apptId, cmbStatus.getValue(), date, time,
                    cmbStatus1.getSelectionModel().getSelectedItem(),
                    cmbProgram.getSelectionModel().getSelectedItem().split(" - ")[0].trim(),
                    therapistId
            );


            if (sessionService.updateSession(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                clearFields();
                generateNewId();
                loadSessionTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleSessionReset(ActionEvent event) {
          clearFields();
    }

    @FXML
    void handleSessionDelete(ActionEvent event) {
        String appointmentId = txtAppointmentId.getText().trim();

        if (appointmentId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please search an Appointment ID to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this session?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = sessionService.deleteSession(appointmentId);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Session Deleted Successfully!").show();
                    clearFields();
                    generateNewId();
                    loadSessionTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete session!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void handleSessionByName(ActionEvent event) {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please Enter name of the Therapist").show();
            return;
        }

        try {
            Therapist therapist = sessionService.getTherapistDetails(searchText);

            if (therapist == null) {
                new Alert(Alert.AlertType.ERROR, "No therapist found").show();
                return;
            }

            List<TherapyScheduleTM> doctorTodaySessions = sessionService.getTodayDoctorSchedule(therapist.getId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TherapistSchedulePopUp.fxml"));
            Parent root = loader.load();

            TherapistSchedulePopUpController popUpController = loader.getController();
            popUpController.setDoctorDetails(therapist.getName(), therapist.getSpecialization(), doctorTodaySessions);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor's Daily Schedule");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            txtSearch.clear();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }


}
