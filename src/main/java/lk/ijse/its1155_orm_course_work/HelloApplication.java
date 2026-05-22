package lk.ijse.its1155_orm_course_work;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.its1155_orm_course_work.dao.custom.UserDAO;
import lk.ijse.its1155_orm_course_work.dao.custom.impl.UserDAOImpl;
import lk.ijse.its1155_orm_course_work.entity.User;
import lk.ijse.its1155_orm_course_work.util.PasswordUtil;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initializeDefaultUser();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        scene.getStylesheets().add(HelloApplication.class.getResource("/style/style.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }
    private void initializeDefaultUser() {
        UserDAO userDAO = new UserDAOImpl();

        if (userDAO.findByUsername("admin") == null) {

            String hashedPassword = PasswordUtil.hashPassword("admin123");

            User defaultAdmin = new User(
                    "U001",
                    "admin",
                    hashedPassword,
                    "admin@serenity.lk",
                    "Admin"
            );

            System.out.println("success");
            boolean isSaved = userDAO.save(defaultAdmin);

            if (isSaved) {
                System.out.println("Default Admin User created successfully!");
            }
        }
    }
}
