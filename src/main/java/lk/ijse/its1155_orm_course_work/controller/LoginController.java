package lk.ijse.its1155_orm_course_work.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.UserService;

import java.io.IOException;

public class LoginController {
    UserService userService = (UserService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.USER);
    @FXML
    private Button btnLogin;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void onLoginClick(ActionEvent event) {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {

            String role = userService.authenticateUser(username, password);

            if (role != null) {
                navigateTo("/view/dashboard.fxml", role);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateTo(String fxmlFile, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            // Pass the role to the dashboard controller
            DashboardController controller = loader.getController();
            controller.setUserRole(role);

            // Debug message to verify role
            System.out.println("User logged in with role: " + role);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
