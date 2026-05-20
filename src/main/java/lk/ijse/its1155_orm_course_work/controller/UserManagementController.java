package lk.ijse.its1155_orm_course_work.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserManagementController {

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirm;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private TextField txtConfirmVisible;
    @FXML private Button btnPwToggle, btnConfirmToggle;

    private boolean isPwVisible = false;
    private boolean isConfirmVisible = false;

    @FXML
    void btnPwToggleOnAction(ActionEvent event) {
        if (!isPwVisible) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            btnPwToggle.setText("🙈");
            isPwVisible = true;
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            btnPwToggle.setText("👁");
            isPwVisible = false;
        }
    }

    @FXML
    void btnConfirmToggleOnAction(ActionEvent event) {
        if (!isConfirmVisible) {
            txtConfirmVisible.setText(txtConfirm.getText());
            txtConfirmVisible.setVisible(true);
            txtConfirmVisible.setManaged(true);
            txtConfirm.setVisible(false);
            txtConfirm.setManaged(false);
            btnConfirmToggle.setText("🙈");
            isConfirmVisible = true;
        } else {
            txtConfirm.setText(txtConfirmVisible.getText());
            txtConfirm.setVisible(true);
            txtConfirm.setManaged(true);
            txtConfirmVisible.setVisible(false);
            txtConfirmVisible.setManaged(false);
            btnConfirmToggle.setText("👁");
            isConfirmVisible = false;
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        String pw = isPwVisible ? txtPasswordVisible.getText() : txtPassword.getText();
        String cpw = isConfirmVisible ? txtConfirmVisible.getText() : txtConfirm.getText();

        if (pw.equals(cpw)) {
            System.out.println("Passwords Match! Saving...");
            // මෙතනදී Hibernate logic එක ලියන්න
        } else {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match!").show();
        }
    }
}