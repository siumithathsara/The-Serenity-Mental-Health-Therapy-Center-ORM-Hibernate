package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.tm.FinancialTM;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapistPerformanceTM;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.ReportService;
import lk.ijse.its1155_orm_course_work.service.custom.impl.ReportServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    private final ReportService reportService =(ReportService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.REPORT);
    @FXML
    private AnchorPane reportsPage;
    @FXML
    private TabPane tabPaneReports;
    @FXML
    private Tab tabAdmin;
    @FXML
    private Tab tabReceptionist;

    //  Admin
    @FXML
    private TableView<TherapistPerformanceTM> tblPerformance;
    @FXML
    private TableColumn<TherapistPerformanceTM, String> colTherapist;
    @FXML
    private TableColumn<TherapistPerformanceTM, Integer> colSessions;
    @FXML
    private TableColumn<TherapistPerformanceTM, Double> colRating;
    @FXML
    private TableColumn<TherapistPerformanceTM, Double> colNoShow;
    @FXML
    private BarChart<String, Number> chartSessions;

    //  Both
    @FXML
    private ComboBox<String> cmbPeriod;
    @FXML
    private TableView<FinancialTM> tblFinancials;
    @FXML
    private TableColumn<FinancialTM, String> colDate;
    @FXML
    private TableColumn<FinancialTM, String> colPatientName;
    @FXML
    private TableColumn<FinancialTM, Double> colAmount;
    @FXML
    private TableColumn<FinancialTM, String> colMethod;
    @FXML
    private PieChart chartRevenue;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        initComboBox();
        setupTableColumns();
        loadDatabaseData();
        checkUserAccess();
    }

    private void initComboBox() {
        if (cmbPeriod != null) {
            int currentMonthValue = LocalDate.now().getMonthValue();
            for (int i = 1; i <= currentMonthValue; i++) {
                String monthName = Month.of(i).name();
                monthName = monthName.substring(0, 1) + monthName.substring(1).toLowerCase();
                cmbPeriod.getItems().add(monthName);
            }
            cmbPeriod.getSelectionModel().selectLast();

            cmbPeriod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    int monthNumber = Month.valueOf(newValue.toUpperCase()).getValue();
                    updateFinancialSection(monthNumber);
                }
            });
    }
    }

    private void setupTableColumns() {
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colSessions.setCellValueFactory(new PropertyValueFactory<>("totalSessions"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("avgRating"));
        colNoShow.setCellValueFactory(new PropertyValueFactory<>("noShowPercentage"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
    }

    private void loadDatabaseData() {
        try {

            tblPerformance.setItems(FXCollections.observableArrayList(reportService.getTherapistPerformanceReport()));
            chartSessions.getData().clear();
            chartSessions.getData().add(reportService.getBarChartData());

            String selectedMonth = cmbPeriod.getSelectionModel().getSelectedItem();
            if (selectedMonth != null) {
                int monthNumber = Month.valueOf(selectedMonth.toUpperCase()).getValue();
                updateFinancialSection(monthNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFinancialSection(int monthNumber) {
        tblFinancials.getItems().clear();
        chartRevenue.getData().clear();

        tblFinancials.setItems(FXCollections.observableArrayList(reportService.getFinancialReport(monthNumber)));
        chartRevenue.getData().addAll(reportService.getPieChartData(monthNumber));
    }

    @FXML
    void btnGenerateFinanceReportOnAction(ActionEvent event) {
        String selectedMonth = cmbPeriod.getSelectionModel().getSelectedItem();
        if (selectedMonth != null) {
            int monthNumber = Month.valueOf(selectedMonth.toUpperCase()).getValue();
            updateFinancialSection(monthNumber);
        }
    }

    private void checkUserAccess() {
        String currentUserRole = "Admin";
        if ("Receptionist".equalsIgnoreCase(currentUserRole)) {
            tabAdmin.setDisable(true);
            tabPaneReports.getSelectionModel().select(tabReceptionist);
        }
    }


}