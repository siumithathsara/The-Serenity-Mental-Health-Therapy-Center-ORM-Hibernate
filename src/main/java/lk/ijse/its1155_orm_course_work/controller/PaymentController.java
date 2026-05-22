package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.PaymentDTO;
import lk.ijse.its1155_orm_course_work.dto.PaymentDetailsDTO;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.PaymentService;
import lk.ijse.its1155_orm_course_work.service.custom.TherapySessionService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    private final PaymentService paymentService = (PaymentService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.PAYMENT);
    private final TherapySessionService sessionService = (TherapySessionService)  ServiceFactory.getInstance().getBO(ServiceFactory.BOType.THERAPY_SESSION);

    @FXML
    private Button btnClear;

    @FXML
    private Button btnProcess;

    @FXML
    private ComboBox<String> cmbPaymentMethod;

    @FXML
    private ComboBox<String> cmbPaymentMethod1;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colAppId;

    @FXML
    private TableColumn<?, ?> colInvNo;

    @FXML
    private TableColumn<?, ?> colMethod;

    @FXML
    private TableColumn<?, ?> colPatient;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private AnchorPane paymentPage;

    @FXML
    private TableView<?> tblPayments;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtInvoiceNo;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextField txtSearch;


    @FXML
    private TextField txtTherapist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbPaymentMethod1.setItems(FXCollections.observableArrayList("Cash", "Card", "Online"));
        loadAllSessionIds();
        txtDate.setText(LocalDate.now().toString());
        txtDate.setEditable(false);
        generatePaymentId();
        clearFields();
    }

    public void setAppointmentId(String appointmentId) {
        if (appointmentId != null) {
            loadAllSessionIds();
            cmbPaymentMethod.setValue(appointmentId);
            cmbPaymentMethod.setDisable(true);
            fetchAndDisplayDetails(appointmentId);
        }
    }

    @FXML
    void handleClearPayment(ActionEvent event) {
           clearFields();
    }

    private void clearFields() {
        cmbPaymentMethod.getSelectionModel().clearSelection();
        cmbPaymentMethod1.getSelectionModel().clearSelection();
        txtPatientName.clear();
        txtTherapist.clear();
        txtAmount.clear();
    }

    private void generatePaymentId(){
        try {
            String nextId = paymentService.generateNextPaymentId();
            txtInvoiceNo.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate Payment ID").show();
        }
    }

    private void loadAllSessionIds() {
        try {

            List<String> ids = sessionService.getAllSessionId();

            ObservableList<String> obList = FXCollections.observableArrayList(ids);
            cmbPaymentMethod.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void loadSessionDetails(ActionEvent event) {
        String selectedSessionId = cmbPaymentMethod.getValue();

        if (selectedSessionId == null) return;

        try {

            PaymentDetailsDTO details = sessionService.getSessionDetailsForPayment(selectedSessionId);

            if (details != null) {

                txtPatientName.setText(details.getPatientName());
                txtTherapist.setText(details.getTherapistName());

                txtAmount.setText(String.valueOf(details.getProgramFee()));

                txtPatientName.setEditable(false);
                txtTherapist.setEditable(false);
                txtAmount.setEditable(false);
            } else {
                new Alert(Alert.AlertType.WARNING, "Session details not found!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while loading details: " + e.getMessage()).show();
        }
    }

    private void fetchAndDisplayDetails(String sessionId) {
        try {
            PaymentDetailsDTO details = sessionService.getSessionDetailsForPayment(sessionId);

            if (details != null) {
                txtPatientName.setText(details.getPatientName());
                txtTherapist.setText(details.getTherapistName());
                txtAmount.setText(String.valueOf(details.getProgramFee()));

                txtPatientName.setEditable(false);
                txtTherapist.setEditable(false);
                txtAmount.setEditable(false);
            } else {
                new Alert(Alert.AlertType.WARNING, "Session details not found!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while loading details: " + e.getMessage()).show();
        }
    }

    @FXML
    void handlePaymentProcess(ActionEvent event) {
        String appointmentId = cmbPaymentMethod.getValue();
        String paymentMethod = cmbPaymentMethod1.getValue();
        String invoiceNo = txtInvoiceNo.getText();
        String amountStr = txtAmount.getText();


        if (appointmentId == null || paymentMethod == null || amountStr.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please Fill All details").show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            LocalDate date = LocalDate.parse(txtDate.getText());


            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setInvoiceNo(invoiceNo);
            paymentDTO.setAmount(amount);
            paymentDTO.setDate(date);
            paymentDTO.setMethod(paymentMethod);
            paymentDTO.setSessionId(appointmentId);

            boolean isSuccess = paymentService.savePaymentAndConfirmSession(paymentDTO);

            if (isSuccess) {
                new Alert(Alert.AlertType.INFORMATION, "Payment success").show();

                clearFields();
                generatePaymentId();

//                paymentPage.getScene().getWindow().hide();
            }

        } catch (Exception e) {

            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}
