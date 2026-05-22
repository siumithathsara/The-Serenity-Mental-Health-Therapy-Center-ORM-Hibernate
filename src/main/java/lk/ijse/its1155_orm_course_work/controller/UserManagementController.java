package lk.ijse.its1155_orm_course_work.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.its1155_orm_course_work.dto.UserDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.UserTM;
import lk.ijse.its1155_orm_course_work.entity.User;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class UserManagementController implements Initializable {
    private final ObservableList<UserTM> userTMList = FXCollections.observableArrayList();
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final String USER_ID_REGEX = "^U\\d{3}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    private final String NAME_REGEX = "^[a-zA-Z\\s]{3,50}$";
    UserService userService = (UserService) ServiceFactory.getInstance().getBO(ServiceFactory.BOType.USER);
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnPwToggle;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<UserTM, String> colEmail;

    @FXML
    private TableColumn<UserTM, String> colId;

    @FXML
    private TableColumn<UserTM, String> colPassword;

    @FXML
    private TableColumn<UserTM, String> colRole;

    @FXML
    private TableColumn<UserTM, String> colUsername;

    @FXML
    private TableView<UserTM> tblUsers;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFullName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUsername;

    @FXML
    private AnchorPane userPage;

    private boolean isPwVisible = false;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadAllUsers();
        clearFields();
        generateNextUserId();
    }

    @FXML
    void handleUserSave(ActionEvent event) {
        if (isPwVisible) {
            txtPassword.setText(txtPasswordVisible.getText());
        }

        String userId = txtUserId.getText().trim();
        String username = txtUsername.getText().trim();
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String role = cmbRole.getValue();
        String password = txtPassword.getText();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill the details").show();
            return;
        }
        if (role == null) {
            new Alert(Alert.AlertType.WARNING, "Please select the Role").show();
            cmbRole.requestFocus();
            return;
        }
        if (!username.matches(USERNAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Username ").show();
            txtUsername.requestFocus();
            return;
        }
        if (!fullName.matches(NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            txtFullName.requestFocus();
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email").show();
            txtEmail.requestFocus();
            return;
        }
        if (password.length() < 4) {
            new Alert(Alert.AlertType.ERROR, "Password too short! (Must be at least 4 characters)").show();
            txtPassword.requestFocus();
            return;
        }
        try {
            UserDTO userDTO = new UserDTO(
                    userId, username, fullName, password, email, role
            );
            boolean isSaved = userService.saveUser(userDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User account created successfully").show();
                //btnResetOnAction(null);
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save the user account").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong ").show();
        }
    }

    private void loadAllUsers() {
        userTMList.clear();
        for (UserDTO dto : userService.getAllUsers()) {
            userTMList.add(new UserTM(
                    dto.getUser_id(),
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getEmail(),
                    dto.getRole()

            ));
        }
        tblUsers.setItems(userTMList);
    }

    @FXML
    void handleUserUpdate(ActionEvent event) {
        if (txtUserId.getText() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user from the table").show();
            return;
        }
        if (isPwVisible) {
            txtPassword.setText(txtPasswordVisible.getText());
        }
        String userId = txtUserId.getText().trim();
        String username = txtUsername.getText().trim();
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String role = cmbRole.getValue();
        String password = txtPassword.getText();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required details!").show();
            return;
        }
        if (role == null) {
            new Alert(Alert.AlertType.WARNING, "Please select the Role").show();
            cmbRole.requestFocus();
            return;
        }
        if (!username.matches(USERNAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Username!").show();
            txtUsername.requestFocus();
            return;
        }
        if (!fullName.matches(NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name!").show();
            txtFullName.requestFocus();
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email!").show();
            txtEmail.requestFocus();
            return;
        }
        if (!password.isEmpty() && password.length() < 4) {
            new Alert(Alert.AlertType.ERROR, "New password too short! (Must be at least 4 characters)").show();
            txtPassword.requestFocus();
            return;
        }

        try {

            UserDTO userDTO = new UserDTO(
                    userId, username, fullName, password, email, role
            );
            boolean isUpdated = userService.updateUser(userDTO);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "User account updated successfully! ").show();
                clearFields();
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update the user account!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong during update!").show();
        }
    }

    private void clearFields() {
        txtUserId.clear();
        txtUserId.setEditable(true);
        txtUsername.clear();
        txtFullName.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        cmbRole.setValue(null);
        tblUsers.getSelectionModel().clearSelection();
    }

    private void generateNextUserId() {
        try {
            String nextId = userService.generateNextUserId();
            txtUserId.setText(nextId);
            txtUserId.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate User ID").show();
        }
    }

    @FXML
    void handleUserReset(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleUserByUsername(ActionEvent event) {
        String searchUsername = txtSearch.getText().trim();

        if (searchUsername.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a username to search!").show();
            return;
        }
        try {
            User user = userService.searchUserByUsername(searchUsername);

            if (user != null) {

                txtUserId.setText(user.getUser_id());
                txtUsername.setText(user.getUsername());
                txtFullName.setText(user.getName());
                txtEmail.setText(user.getEmail());
                cmbRole.setValue(user.getRole());
                txtPassword.setText(user.getPassword());

            } else {

                new Alert(Alert.AlertType.ERROR, "User not found!").show();
                clearFields();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong while searching!").show();
        }
    }

    @FXML
    void handleUserDelete(ActionEvent event) {
        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select or search a user first!").show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setTitle("Delete Confirmation");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {

                    boolean isDeleted = userService.deleteUser(userId);

                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
                        clearFields();
                        loadAllUsers();
                        generateNextUserId();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete the user!").show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong during deletion!").show();
                }
            }
        });
    }

}