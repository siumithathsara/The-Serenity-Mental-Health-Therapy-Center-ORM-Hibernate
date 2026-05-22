package lk.ijse.its1155_orm_course_work.config;

import lk.ijse.its1155_orm_course_work.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Patient.class)
                .addAnnotatedClass(TherapySession.class)
                .addAnnotatedClass(Therapist.class)
                .addAnnotatedClass(TherapyProgram.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        return (factoryConfiguration == null) ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
