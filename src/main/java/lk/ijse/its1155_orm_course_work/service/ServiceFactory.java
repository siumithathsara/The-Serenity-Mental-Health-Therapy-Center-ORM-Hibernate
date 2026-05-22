package lk.ijse.its1155_orm_course_work.service;

import lk.ijse.its1155_orm_course_work.service.custom.impl.*;

public class ServiceFactory {
    private static ServiceFactory boFactory;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return (boFactory == null) ? (boFactory = new ServiceFactory()) : boFactory;
    }

    public SuperService getBO(BOType type) {
        switch (type) {
            case PATIENT:
                return new PatientServiceImpl();
            case REPORT:
                return new ReportServiceImpl();
            case PAYMENT:
                return new PaymentServiceImpl();
            case THERAPIST:
                return new TherapistServiceImpl();
            case DASHBOARD:
                return new DashBoardServiceImpl();

            case THERAPY_PROGRAM:
                return new TherapyProgramServiceImpl();
            case THERAPY_SESSION:
                return new TherapySessionServiceImpl();
            case USER:
                return new UserServiceImpl();
            default:
                return null;
        }
    }

    public enum BOType {
        PATIENT,
        REPORT,
        PAYMENT,
        THERAPIST,
        DASHBOARD,
        THERAPY_PROGRAM,
        THERAPY_SESSION,
        USER
    }
}
