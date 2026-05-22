package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.TherapyProgramingService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TherapyProgramManagementController implements Initializable {

    private final TherapyProgramingService therapyProgramingService = (TherapyProgramingService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_PROGRAM);

    private final String PROGRAM_ID_REGEX = "^[A-Z]{1,4}\\d{3,5}$";
    private final String PROGRAM_NAME_REGEX = "^[A-Za-z\\s\\-&(),.]{3,100}$";
    private final String PROGRAM_DURATION_REGEX = "^\\d+\\s+(week|weeks|month|months|day|days|hour|hours|session|sessions)$";
    private final String PROGRAM_FEE_REGEX = "^\\d+(\\.\\d{1,2})?$";

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<String, TherapyProgramDTO> colDuration;

    @FXML
    private TableColumn<Double, TherapyProgramDTO> colFee;

    @FXML
    private TableColumn<String, TherapyProgramDTO> colId;

    @FXML
    private TableColumn<String, TherapyProgramDTO> colName;

    @FXML
    private AnchorPane programPage;

    @FXML
    private TableView<TherapyProgramDTO> tblPrograms;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtProgramId;

    @FXML
    private TextField txtProgramName;

    @FXML
    private TextField txtSearch;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        generateProgramId();
        loadProgramTable();

    }

    @FXML
    void handleProgramSave(ActionEvent event) {

        String id = txtProgramId.getText().trim();
        String name = txtProgramName.getText().trim();
        String duration = txtDuration.getText().trim();
        String feeStr = txtFee.getText().trim();
        String description = txtDescription.getText().trim();


        if (!id.matches(PROGRAM_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Program ID! (Eg: MT1001)").show();
        } else if (!name.matches(PROGRAM_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Program Name!").show();
        } else if (!duration.matches(PROGRAM_DURATION_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Duration! (Eg: '12 weeks' or '6 months')").show();
        } else if (!feeStr.matches(PROGRAM_FEE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Fee Amount! (Eg: 80000 or 80000.00)").show();
        } else if (description.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Description cannot be empty!").show();
        } else {

            try {

                double fee = Double.parseDouble(feeStr);

                TherapyProgramDTO programDTO = new TherapyProgramDTO(id, name, duration, fee, description);

                boolean result = therapyProgramingService.saveProgram(programDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Program saved successfully!").show();

                    generateProgramId();
                    loadProgramTable();
                    clearField();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong while saving!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            }
        }
    }

    private void generateProgramId() {

        try {
            String nextId = therapyProgramingService.generateNextCustomerId();
            txtProgramId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Patient ID").show();
        }
    }

    private void loadProgramTable() {
        try {

            List<TherapyProgramDTO> programDTOList = therapyProgramingService.getPrograms();

            ObservableList<TherapyProgramDTO> obList = FXCollections.observableArrayList();

            for (TherapyProgramDTO programDTO : programDTOList) {
                obList.add(programDTO);
            }

            tblPrograms.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearField() {
        txtProgramName.setText("");
        txtDuration.setText("");
        txtFee.setText("");
        txtProgramName.setText("");
        txtDescription.setText("");
    }

    @FXML
    void handleResetBtn(ActionEvent event) {

        clearField();
    }

    @FXML
    void handleProgramSearch(KeyEvent event) {

        try {
            if (event.getCode() == KeyCode.ENTER) {


                String id = txtProgramId.getText().trim();

                if (!id.matches(PROGRAM_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {
                    TherapyProgramDTO programDTO = therapyProgramingService.searchProgram(id);

                    if (programDTO != null) {

                        txtProgramName.setText(programDTO.getProgramName());
                        txtDuration.setText(programDTO.getDuration());
                        txtFee.setText(Double.toString(programDTO.getFee()));
                        txtDescription.setText(programDTO.getDescription());

                    } else {

                        new Alert(Alert.AlertType.ERROR, "Program not found!").show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    @FXML
    void handleProgramUpdate(ActionEvent event) {

        String id = txtProgramId.getText().trim().toUpperCase();
        String programName = txtProgramName.getText().trim();
        String duration = txtDuration.getText().trim();
        String description = txtDescription.getText().trim();
        String feeStr = txtFee.getText().trim();


        if (!id.matches(PROGRAM_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Program ID").show();
        } else if (programName.isEmpty() || !programName.matches(PROGRAM_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Program Name").show();
        } else if (duration.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Duration cannot be empty").show();
        } else if (feeStr.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fee cannot be empty").show();
        } else {
            try {

                double fee = Double.parseDouble(feeStr);

                TherapyProgramDTO programDTO = new TherapyProgramDTO(id, programName, duration, fee, description);

                boolean result = therapyProgramingService.updateProgram(programDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Program updated successfully!").show();

                    generateProgramId();
                    loadProgramTable();
                    clearField();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update the program!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid format for Fee! Please enter a valid number.").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    @FXML
    void handleProgramDelete(ActionEvent event) {
        String programId = txtProgramId.getText().trim();

        if (!programId.matches(PROGRAM_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Patient ID").show();
        } else {

            try {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Are you sure to delete this Patient ?");
                confirmAlert.setContentText("Patient ID: " + programId);

                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean result1 = therapyProgramingService.deleteCustomer(programId);

                    if (result1) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                        clearField();
                        loadProgramTable();
                        generateProgramId();
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

}
