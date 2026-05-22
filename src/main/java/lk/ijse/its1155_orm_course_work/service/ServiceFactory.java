package lk.ijse.its1155_orm_course_work.service;

import lk.ijse.its1155_orm_course_work.service.custom.impl.*;

public class ServiceFactory {
    private static ServiceFactory boFactory;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return (boFactory == null) ? (boFactory = new ServiceFactory()) : boFactory;
    }

    public enum BOType {
        PATIENT,
        REPORT,
        PAYMENT,
        THERAPIST,
        THERAPIST_AVAILABILITY,
        THERAPIST_PROGRAM,
        THERAPY_PROGRAM,
        THERAPY_SESSION,
        USER
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
            case THERAPIST_AVAILABILITY:
//                return new TherapistAvailabilityBOImpl();
            case THERAPIST_PROGRAM:
//                return new TherapistProgramBOImpl();
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
}
