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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapistTM;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.TherapistService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapyProgramingService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TherapistManagementController implements Initializable {

    private final TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPIST);
    private final TherapyProgramingService therapyProgramingService = (TherapyProgramingService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_PROGRAM);

    private final String THERAPIST_ID_REGEX = "^T\\d{3}$";
    private final String THERAPIST_NAME_REGEX = "^[A-Za-z.]+(?:\\s[A-Za-z.]{2,})+$";
    private final String THERAPIST_CONTACT_REGEX = "^(07\\d{8}|\\+947\\d{8})$";
    private final String THERAPIST_EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private CheckBox chkFri;

    @FXML
    private CheckBox chkMon;

    @FXML
    private CheckBox chkSat;

    @FXML
    private CheckBox chkSun;

    @FXML
    private CheckBox chkThu;

    @FXML
    private CheckBox chkTue;

    @FXML
    private CheckBox chkWed;

    @FXML
    private ComboBox<String> cmbFilterDay;

    @FXML
    private ComboBox<String> cmbSpecialization;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableColumn<TherapistTM, String> colDay;

    @FXML
    private TableColumn<TherapistTM, String> colId;

    @FXML
    private TableColumn<TherapistTM, String> colName;

    @FXML
    private TableColumn<TherapistTM, String> colPhone;

    @FXML
    private TableColumn<TherapistTM, String> colSpecialization;

    @FXML
    private TableColumn<TherapistTM, String> colStatus;

    @FXML
    private TableView<TherapistTM> tblTherapists;

    @FXML
    private AnchorPane therapistPage;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTherapistId;

    @FXML
    private VBox vboxForm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Therapist Page is Loaded");

        ObservableList<String> statusList = FXCollections.observableArrayList("Active", "Inactive");
        cmbStatus.setItems(statusList);

        cmbFilterDay.setItems(FXCollections.observableArrayList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        ).sorted());

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("workingDays"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadSpecialization();
        generatePatientID();
        loadTherapistTable();

        tblTherapists.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                javafx.application.Platform.runLater(() -> {
                    txtTherapistId.setText(newValue.getId());

                    String rawName = newValue.getName();
                    if (rawName != null && rawName.startsWith("Dr. ")) {
                        rawName = rawName.replaceFirst("Dr. ", "").trim();
                    }
                    txtName.setText(rawName);

                    cmbSpecialization.setValue(newValue.getSpecialization());
                    cmbStatus.setValue(newValue.getStatus());
                    txtPhone.setText(newValue.getPhone());

                    if (newValue.getWorkingDays() != null) {
                        chkMon.setSelected(newValue.getWorkingDays().contains("Mon"));
                        chkTue.setSelected(newValue.getWorkingDays().contains("Tue"));
                        chkWed.setSelected(newValue.getWorkingDays().contains("Wed"));
                        chkThu.setSelected(newValue.getWorkingDays().contains("Thu"));
                        chkFri.setSelected(newValue.getWorkingDays().contains("Fri"));
                        chkSat.setSelected(newValue.getWorkingDays().contains("Sat"));
                        chkSun.setSelected(newValue.getWorkingDays().contains("Sun"));
                    }

                    try {
                        TherapistDTO dto = therapistService.searchTherapist(newValue.getId());
                        if (dto != null) {
                            txtEmail.setText(dto.getEmail());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private void loadSpecialization() {
        try {
            List<TherapyProgramDTO> therapistList = therapyProgramingService.getPrograms();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (TherapyProgramDTO therapistDTO : therapistList) {
                obList.add(String.valueOf(therapistDTO.getProgramName()));
            }

            cmbSpecialization.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void handleTherapistSave(ActionEvent event) {
        String id = txtTherapistId.getText();
        String name = txtName.getText().trim();
        String specialization = cmbSpecialization.getValue();
        String status = (cmbStatus.getValue() != null) ? cmbStatus.getValue().toString() : "";
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        java.util.List<String> workingDays = new java.util.ArrayList<>();
        if (chkMon.isSelected()) workingDays.add("Mon");
        if (chkTue.isSelected()) workingDays.add("Tue");
        if (chkWed.isSelected()) workingDays.add("Wed");
        if (chkThu.isSelected()) workingDays.add("Thu");
        if (chkFri.isSelected()) workingDays.add("Fri");
        if (chkSat.isSelected()) workingDays.add("Sat");
        if (chkSun.isSelected()) workingDays.add("Sun");

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || specialization == null || status.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").show();
            return;
        }

        if (!name.matches(THERAPIST_NAME_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Name format! (e.g., Dr. Saman Perera)").show();
            txtName.requestFocus();
            return;
        }

        if (workingDays.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one working day!").show();
            return;
        }

        if (!phone.matches(THERAPIST_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Contact Number! (e.g., 0771234567)").show();
            txtPhone.requestFocus();
            return;
        }

        if (!email.matches(THERAPIST_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email Address!").show();
            txtEmail.requestFocus();
            return;
        }

        try {
            lk.ijse.its1155_orm_course_work.dto.TherapistDTO therapistDTO =
                    new lk.ijse.its1155_orm_course_work.dto.TherapistDTO(id, name, specialization, status, phone, email, workingDays);

            boolean isSaved = therapistService.saveTherapist(therapistDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Therapist Saved Successfully!").show();
                clearFields();
                generatePatientID();
                loadTherapistTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the Therapist!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        cmbSpecialization.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();

        chkMon.setSelected(false);
        chkTue.setSelected(false);
        chkWed.setSelected(false);
        chkThu.setSelected(false);
        chkFri.setSelected(false);
        chkSat.setSelected(false);
        chkSun.setSelected(false);
    }

    private void generatePatientID() {
        try {
            String nextId = therapistService.generateNextCustomerId();
            txtTherapistId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Therapist ID").show(); // 🌟 මෙතන වෙනස් කළා
        }
    }

    private void loadTherapistTable() {
        try {
            // 🌟 Service එකට null පාස් කරලා, මුළු සේවාදායකයින් ලිස්ට් එකම ගන්න හැදුවා
            List<TherapistDTO> dtoList = therapistService.getAllTherapists(null);

            ObservableList<TherapistTM> tmList = FXCollections.observableArrayList();

            for (TherapistDTO dto : dtoList) {
                String daysString = (dto.getWorkingDays() != null) ? String.join(", ", dto.getWorkingDays()) : "";
                String displayName = "Dr. " + dto.getName();
                tmList.add(new TherapistTM(
                        dto.getId(),
                        displayName,
                        dto.getSpecialization(),
                        daysString,
                        dto.getStatus(),
                        dto.getPhone()
                ));
            }

            tblTherapists.setItems(tmList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load therapists to table!").show();
        }
    }

    @FXML
    void handleTherapistReset(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleTherapistSearch(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {
                String id = txtTherapistId.getText().trim();

                if (!id.matches(THERAPIST_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Therapist ID").show();
                } else {

                    chkMon.setSelected(false);
                    chkTue.setSelected(false);
                    chkWed.setSelected(false);
                    chkThu.setSelected(false);
                    chkFri.setSelected(false);
                    chkSat.setSelected(false);
                    chkSun.setSelected(false);

                    TherapistDTO therapistDTO = therapistService.searchTherapist(id);

                    if (therapistDTO != null) {
                        txtName.setText(therapistDTO.getName());
                        cmbSpecialization.setValue(therapistDTO.getSpecialization());
                        txtPhone.setText(therapistDTO.getPhone());
                        txtEmail.setText(therapistDTO.getEmail());
                        cmbStatus.setValue(therapistDTO.getStatus());

                        if (therapistDTO.getWorkingDays() != null) {
                            java.util.List<String> days = therapistDTO.getWorkingDays();

                            chkMon.setSelected(days.contains("Mon"));
                            chkTue.setSelected(days.contains("Tue"));
                            chkWed.setSelected(days.contains("Wed"));
                            chkThu.setSelected(days.contains("Thu"));
                            chkFri.setSelected(days.contains("Fri"));
                            chkSat.setSelected(days.contains("Sat"));
                            chkSun.setSelected(days.contains("Sun"));
                        }

                    } else {
                        new Alert(Alert.AlertType.ERROR, "Therapist not found!").show();
                        clearFields();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    void handleTherapistUpdate(ActionEvent event) {
        String therapistId = txtTherapistId.getText().trim();
        String therapistName = txtName.getText().trim();
        String specialization = cmbSpecialization.getValue();
        String status = cmbStatus.getValue();
        String therapistPhone = txtPhone.getText().trim();
        String therapistEmail = txtEmail.getText().trim();

        java.util.List<String> workingDays = new java.util.ArrayList<>();
        if (chkMon.isSelected()) workingDays.add("Mon");
        if (chkTue.isSelected()) workingDays.add("Tue");
        if (chkWed.isSelected()) workingDays.add("Wed");
        if (chkThu.isSelected()) workingDays.add("Thu");
        if (chkFri.isSelected()) workingDays.add("Fri");
        if (chkSat.isSelected()) workingDays.add("Sat");
        if (chkSun.isSelected()) workingDays.add("Sun");

        if (therapistName.isEmpty() || therapistPhone.isEmpty() || therapistEmail.isEmpty() || specialization == null || status == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").show();
            return;
        } if (therapistName.startsWith("Dr. ")) {
            therapistName = therapistName.replaceFirst("Dr. ", "").trim();
        } if (!therapistId.matches(THERAPIST_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Therapist ID").show();
            return;
        } if (!therapistName.matches(THERAPIST_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Therapist name").show();
            txtName.requestFocus();
            return;
        } if (workingDays.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one working day!").show();
            return;
        } if (!therapistPhone.matches(THERAPIST_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Therapist Phone").show();
            txtPhone.requestFocus();
            return;
        } if (!therapistEmail.matches(THERAPIST_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email Address").show();
            txtEmail.requestFocus();
            return;
        }

        try {
            TherapistDTO therapistDTO = new TherapistDTO(
                    therapistId,
                    therapistName,
                    specialization,
                    status,
                    therapistPhone,
                    therapistEmail,
                    workingDays
            );

            boolean result = therapistService.updateTherapist(therapistDTO);

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Therapist updated successfully!").show();
                clearFields();
                generatePatientID();
                loadTherapistTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update therapist!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!: ").show();
        }
    }

    @FXML
    void handleTherapistDelete(ActionEvent event) {
        String therapistId = txtTherapistId.getText().trim();

        if (!therapistId.matches(THERAPIST_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Therapist ID").show();
        } else {
            try {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure you want to delete this Therapist?");
                confirmAlert.setContentText("Therapist ID: " + therapistId);

                java.util.Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean isDeleted = therapistService.deleteTherapist(therapistId);

                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Therapist deleted successfully!").show();
                        clearFields();
                        loadTherapistTable();
                        generatePatientID();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete the Therapist!").show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void handleSearchByName(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String typedText = txtSearch.getText().trim();

            if (typedText.isEmpty()) {
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TherapistPopUp.fxml"));
                Parent root = loader.load();

                TherapistPopUpController popupController = loader.getController();

                if (popupController != null) {
                    popupController.initSearchText(typedText);
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Available Therapists");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

                txtSearch.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleSearchByDate(ActionEvent event) {
        String typedText = txtSearch.getText().trim();
        String selectedDay = cmbFilterDay.getValue();

        if (typedText.isEmpty() && selectedDay == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TherapistPopUp.fxml"));
            Parent root = loader.load();

            TherapistPopUpController popupController = loader.getController();

            if (popupController != null) {
                // 🌟 2. Parameter 3ක් ඉල්ලන නිසා, මෙතනට 'null' කියලා තුන්වෙනි එක පාස් කළා (මොකද මේ පේජ් එකේ programId එකක් නැහැ)
                popupController.initSearchData(typedText, selectedDay, null);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Available Therapists");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            txtSearch.clear();
            cmbFilterDay.getSelectionModel().clearSelection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}