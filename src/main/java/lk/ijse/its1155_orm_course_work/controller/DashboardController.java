package lk.ijse.its1155_orm_course_work.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane mainPage;

    @FXML
    private AnchorPane majorPage;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Button programPageBtn;

    @FXML
    private Button therapistPageBtn;

    @FXML
    private Button userPageBtn;



    public void initialize(URL url, ResourceBundle rb) {
        updateDateTime();
    }

    public void setUserRole(String role) {
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        if (isAdmin) {
           therapistPageBtn.setDisable(false);
            programPageBtn.setDisable(false);;
            userPageBtn.setDisable(false);;
        } else {
            therapistPageBtn.setDisable(true);;
            programPageBtn.setDisable(true);
            userPageBtn.setDisable(true);
        }


        System.out.println("Admin access: " + isAdmin);
    }

    @FXML
    void patientPageBtn(ActionEvent event) {

        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/patientManagement.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void therapistPageBtn(ActionEvent event) {

        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/therapistsPage.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void programPageBtn(ActionEvent event) {
        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/therapyProgramPage.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homePageBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
        majorPage.getChildren().setAll(pane);
    }
    @FXML
    void logoutBtn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/login.fxml"));
                    javafx.scene.Parent root = loader.load();

                    javafx.stage.Stage stage = (javafx.stage.Stage) majorPage.getScene().getWindow();
                    stage.setScene(new javafx.scene.Scene(root));
                    stage.centerOnScreen();
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to load the login page!").show();
                }
            }
        });
    }

    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        lblTime.setText(currentTime.format(timeFormatter));
        lblDate.setText(currentDate.format(dateFormatter));
    }

    @FXML
    void SessionPageBtn(ActionEvent event) {
        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/therapySession.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void UserPageBtn(ActionEvent event) {
        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userManagement.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void paymentPageBtn(ActionEvent event) {
        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/paymentPage.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reportPageBtn(ActionEvent event) {
        try {
            mainPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/ReportPage.fxml"));
            anchorPane.prefHeightProperty().bind(mainPage.heightProperty());
            anchorPane.prefWidthProperty().bind(mainPage.widthProperty());
            mainPage.getChildren().add(anchorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
