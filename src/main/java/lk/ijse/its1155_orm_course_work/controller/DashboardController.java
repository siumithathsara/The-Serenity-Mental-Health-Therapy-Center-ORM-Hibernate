package lk.ijse.its1155_orm_course_work.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    public void initialize(URL url, ResourceBundle rb) {
        updateDateTime();
    }

    public void setUserRole(String role) {
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

//        if (isAdmin) {
//            therapistsPageBtn.setVisible(true);
//            therapyProgramPageBtn.setVisible(true);
//            usersPageBtn.setVisible(true);
//        } else {
//            therapistsPageBtn.setVisible(false);
//            therapyProgramPageBtn.setVisible(false);
//            usersPageBtn.setVisible(false);
//        }


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


}
