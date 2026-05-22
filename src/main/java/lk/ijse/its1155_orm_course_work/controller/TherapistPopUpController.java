package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapistTM;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.TherapistService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TherapistPopUpController implements Initializable {

    private final ObservableList<TherapistTM> masterDataList = FXCollections.observableArrayList();
    private final TherapistService therapistService = (TherapistService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPIST);
    @FXML
    private ComboBox<String> cmbFilterDay;
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
    private AnchorPane therapistViewPage;
    @FXML
    private TextField txtSearch;
    private FilteredList<TherapistTM> filteredDataList;
    private String programId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("workingDays"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        cmbFilterDay.setItems(FXCollections.observableArrayList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        ));

        filteredDataList = new FilteredList<>(masterDataList, p -> true);
        tblTherapists.setItems(filteredDataList);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        cmbFilterDay.valueProperty().addListener((observable, oldValue, newValue) -> applyFilter());
    }


    public void initSearchText(String searchText) {

        this.programId = null;
        loadAllTherapists();

        if (searchText != null && !searchText.trim().isEmpty()) {
            txtSearch.setText(searchText);
            applyFilter();
        }
    }

    public void initSearchData(String searchText, String searchDay, String programId) {
        this.programId = programId;

        javafx.application.Platform.runLater(() -> {
            tblTherapists.getSelectionModel().clearSelection();
        });

        if (searchText != null && !searchText.trim().isEmpty()) {
            txtSearch.setText(searchText);
        }
        if (searchDay != null && !searchDay.trim().isEmpty()) {
            cmbFilterDay.setValue(searchDay);
        }

        loadAllTherapists();
        applyFilter();
    }

    private void loadAllTherapists() {
        masterDataList.clear();
        try {

            List<TherapistDTO> allTherapists = therapistService.getAllTherapists(programId);

            if (allTherapists != null) {
                for (TherapistDTO dto : allTherapists) {
                    String days = (dto.getWorkingDays() != null) ? String.join(", ", dto.getWorkingDays()) : "Not Set";
                    masterDataList.add(new TherapistTM(
                            dto.getId(), dto.getName(), dto.getSpecialization(), days, dto.getStatus(), dto.getPhone()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFilter() {
        String searchText = (txtSearch.getText() == null) ? "" : txtSearch.getText().toLowerCase().trim();
        String selectedDay = cmbFilterDay.getValue();

        String shortDay = null;
        if (selectedDay != null && !selectedDay.isEmpty()) {
            shortDay = selectedDay.substring(0, 3);
        }

        final String finalShortDay = shortDay;

        filteredDataList.setPredicate(therapist -> {

            boolean matchesSearch = searchText.isEmpty() ||
                    (therapist.getId() != null && therapist.getId().toLowerCase().contains(searchText)) ||
                    (therapist.getName() != null && therapist.getName().toLowerCase().contains(searchText)) ||
                    (therapist.getSpecialization() != null && therapist.getSpecialization().toLowerCase().contains(searchText));

            boolean matchesDay = (finalShortDay == null) ||
                    (therapist.getWorkingDays() != null && therapist.getWorkingDays().contains(finalShortDay));

            return matchesSearch && matchesDay;
        });
    }

    @FXML
    void handleResetView(ActionEvent event) {
        txtSearch.clear();
        cmbFilterDay.getSelectionModel().clearSelection();
        cmbFilterDay.setPromptText("Select Day");
        loadAllTherapists();
    }
}