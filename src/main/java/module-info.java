module lk.ijse.its1155_orm_course_work {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires jbcrypt;
    requires jakarta.persistence;
    requires java.naming;
    requires javafx.base;
    requires javafx.graphics;

    requires net.sf.jasperreports.core;



    opens lk.ijse.its1155_orm_course_work to javafx.fxml;
    opens lk.ijse.its1155_orm_course_work.dto to javafx.base, javafx.fxml, org.hibernate.orm.core;
    opens lk.ijse.its1155_orm_course_work.controller to javafx.fxml;
    opens lk.ijse.its1155_orm_course_work.dto.tm to javafx.base, javafx.fxml;
    opens lk.ijse.its1155_orm_course_work.entity to org.hibernate.orm.core;
    exports lk.ijse.its1155_orm_course_work;
    exports lk.ijse.its1155_orm_course_work.controller;
}