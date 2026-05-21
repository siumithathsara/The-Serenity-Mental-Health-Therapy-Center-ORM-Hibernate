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
import lk.ijse.its1155_orm_course_work.dto.PatientDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.PatientHistoryTM;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.PatientService;


import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientManagementController implements Initializable {
    private final PatientService patientService = (PatientService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.PATIENT);

    private final String PATIENT_ID_REGEX = "^P\\d{3}$";
    private final String PATIENT_NAME_REGEX = "^[A-Za-z]{3,}(?:\\s[A-Za-z]{3,})+$";
    private final String PATIENT_ADDRESS_REGEX = "^[A-Za-z.-]{2,}$";
    private final String PATIENT_NIC_REGEX = "^(\\d{9}[vV]|\\d{12})$";
    private final String PATIENT_CONTACT_REGEX = "^(07\\d{8}|\\+947\\d{8})$";

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<PatientDTO, String> colAddress;

    @FXML
    private TableColumn<PatientDTO, Date> colDate;

    @FXML
    private TableColumn<PatientDTO, String> colId;

    @FXML
    private TableColumn<PatientDTO, String> colName;

    @FXML
    private TableColumn<PatientDTO, String> colNic;

    @FXML
    private TableColumn<PatientDTO, String> colPhone;

    @FXML
    private DatePicker dtpRegDate;

    @FXML
    private AnchorPane patientPage;

    @FXML
    private TableView<PatientDTO> tblPatients;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPatientId;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Patient page is loaded");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("registeredDate"));
        dtpRegDate.setValue(LocalDate.now());
        dtpRegDate.getEditor().setDisable(true);
        dtpRegDate.getEditor().setStyle("-fx-opacity: 1;");

        generatePatientID();
        loadPatientTable();

    }

    @FXML
    void handlePatientSave() {
        String patientId = txtPatientId.getText().trim();
        String patientName = txtPatientName.getText().trim();
        String patientNic = txtNic.getText().trim();
        String patientPhone = txtPhone.getText().trim();
        String patientAddress = txtAddress.getText().trim();
        LocalDate regDate = dtpRegDate.getValue();

        if (!patientId.matches(PATIENT_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient ID").show();
        } else if (!patientName.matches(PATIENT_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient name").show();
        } else if (!patientNic.matches(PATIENT_NIC_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient NIC").show();
        } else if (!patientPhone.matches(PATIENT_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient Phone").show();
        } else if (!patientAddress.matches(PATIENT_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient Address").show();
        } else if (regDate == null) {
            new Alert(Alert.AlertType.ERROR, "Please select registration date").show();
        } else {
            try {
                PatientDTO patientDTO = new PatientDTO(patientId, patientName, patientNic, patientPhone, patientAddress, regDate);
                boolean result = patientService.savePatient(patientDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Patient saved successfully!").show();

                    generatePatientID();
                    loadPatientTable();
                    clearField();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();

            }

        }


    }

    private void generatePatientID() {
        try {
            String nextId = patientService.generateNextCustomerId();
            txtPatientId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Patient ID").show();
        }
    }

    private void loadPatientTable() {

        try {

            List<PatientDTO> patientDTOList = patientService.getPatients();

            ObservableList<PatientDTO> obList = FXCollections.observableArrayList();

            for (PatientDTO patientDTO : patientDTOList) {
                obList.add(patientDTO);
            }

            tblPatients.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearField() {
        // txtPatientId.setText("");
        txtPatientName.setText("");
        txtNic.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        dtpRegDate.setValue(LocalDate.now());
    }

    @FXML
    void handlePatientReset(ActionEvent event) {

        clearField();
    }

    @FXML
    void handlePatientUpdate(ActionEvent event) {

        String patientId = txtPatientId.getText().trim();
        String patientName = txtPatientName.getText().trim();
        String patientNic = txtNic.getText().trim();
        String patientPhone = txtPhone.getText().trim();
        String patientAddress = txtAddress.getText().trim();
        LocalDate regDate = dtpRegDate.getValue();

        if (!patientId.matches(PATIENT_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient ID").show();
        } else if (!patientName.matches(PATIENT_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient name").show();
        } else if (!patientNic.matches(PATIENT_NIC_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient NIC").show();
        } else if (!patientPhone.matches(PATIENT_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient Phone").show();
        } else if (!patientAddress.matches(PATIENT_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient Address").show();
        } else if (regDate == null) {
            new Alert(Alert.AlertType.ERROR, "Please select registration date").show();
        } else {
            try {
                PatientDTO patientDTO = new PatientDTO(patientId, patientName, patientNic, patientPhone, patientAddress, regDate);
                boolean result = patientService.updatePatient(patientDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Patient update successfully!").show();

                    generatePatientID();
                    loadPatientTable();
                    clearField();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();

            }

        }
    }

    @FXML
    void handlePatientSearch(KeyEvent event) {

        try {
            if (event.getCode() == KeyCode.ENTER) {


                String id = txtPatientId.getText().trim();

                if (!id.matches(PATIENT_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {
                    PatientDTO patientDTO = patientService.searchPatient(id);

                    if (patientDTO != null) {

                        txtPatientName.setText(patientDTO.getName());
                        txtNic.setText(patientDTO.getNic());
                        txtPhone.setText(patientDTO.getPhone());
                        txtAddress.setText(patientDTO.getAddress());
                        dtpRegDate.setValue(patientDTO.getRegisteredDate());
                    } else {

                        new Alert(Alert.AlertType.ERROR, "Patient not found!").show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void handlePatientDelete(ActionEvent event) {

        String patientId = txtPatientId.getText().trim();

        if (!patientId.matches(PATIENT_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this Patient ?");
                confirmAlert.setContentText("Patient ID: " + patientId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = patientService.deleteCustomer(patientId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                        clearField();
                        loadPatientTable();
                        generatePatientID();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    @FXML
    void handleSearchByName(ActionEvent event) {
        String searchText = txtSearch.getText().trim();

        if (searchText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please Enter patient Name.").show();
            return;
        }

        try {

            Patient patient = patientService.searchPatientByName(searchText);

            if (patient == null) {
                new Alert(Alert.AlertType.ERROR, "Patient Name not found").show();
                return;
            }


            List<PatientHistoryTM> historyList = patientService.getPatientHistory(patient.getId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PatientPopUp.fxml"));
            Parent root = loader.load();

            PatientPopUpController popUpController = loader.getController();


            popUpController.setPatientData(
                    patient.getName(),
                    patient.getNic(),
                    patient.getPhone(),
                    historyList
            );


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Patient Treatment History - " + patient.getName());
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
