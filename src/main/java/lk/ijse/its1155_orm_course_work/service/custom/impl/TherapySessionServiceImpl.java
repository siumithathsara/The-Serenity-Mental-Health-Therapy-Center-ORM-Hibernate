package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.PatientDAO;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapistDAO;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapyProgramDAO;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.dto.PaymentDetailsDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapySessionDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapyScheduleTM;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import lk.ijse.its1155_orm_course_work.entity.TherapyProgram;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import lk.ijse.its1155_orm_course_work.service.custom.TherapySessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TherapySessionServiceImpl implements TherapySessionService {
    private final TherapySessionDAO therapySessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    private PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);
    private TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public List<String> getBookedTimeSlots(String therapistId, LocalDate selectedDate) throws Exception {
        return therapySessionDAO.getBookedTimeSlots(therapistId, selectedDate);
    }

    @Override
    public String generateNextSessionId() throws Exception {
        return therapySessionDAO.generateNextId();
    }

    public boolean bookSession(TherapySessionDTO dto) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            boolean isBooked = therapySessionDAO.isSlotBooked(dto.getTherapistId(), dto.getSessionDate(), dto.getTimeSlot(), session);

            if (isBooked) {

                transaction.rollback();
                return false;
            }

            Patient patient = session.get(Patient.class, dto.getPatientId());
            Therapist therapist = session.get(Therapist.class, dto.getTherapistId());
            TherapyProgram program = session.get(TherapyProgram.class, dto.getProgramId());

            if (patient == null || therapist == null || program == null) {
                transaction.rollback();
                return false;
            }

            TherapySession therapySession = new TherapySession();
            therapySession.setAppointmentId(dto.getAppointmentId());
            therapySession.setStatus(dto.getStatus());
            therapySession.setSessionDate(dto.getSessionDate());
            therapySession.setTimeSlot(dto.getTimeSlot());
            therapySession.setPatient(patient);
            therapySession.setTherapist(therapist);
            therapySession.setProgram(program);

            boolean isSaved = therapySessionDAO.save(therapySession, session);

            if (isSaved) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isSlotBooked(String therapistId, LocalDate sessionDate, String cleanTimeSlot) {
        Session session = FactoryConfiguration.getInstance().getSession();

        try {

            return therapySessionDAO.isSlotBooked(therapistId, sessionDate, cleanTimeSlot, session);
        } catch (Exception e) {
            throw e;
        } finally {

            session.close();
        }
    }

    @Override
    public List<TherapySessionDTO> getAllSession() throws Exception {
        List<TherapySession> allEntities = therapySessionDAO.getAll();

        List<TherapySessionDTO> sessionDTOList = new ArrayList<>();

        for (TherapySession s : allEntities) {
            sessionDTOList.add(new TherapySessionDTO(
                    s.getAppointmentId(),
                    s.getStatus(),
                    s.getSessionDate(),
                    s.getTimeSlot(),
                    s.getPatient().getId(),
                    s.getPatient().getName(),
                    s.getProgram().getId(),
                    s.getProgram().getProgramName(),
                    s.getTherapist().getId(),
                    s.getTherapist().getName()
            ));
        }

        return sessionDTOList;
    }

    @Override
    public TherapySessionDTO searchSession(String appointmentId) throws Exception {
        TherapySession entity = therapySessionDAO.search(appointmentId);
        if (entity != null) {
            return new TherapySessionDTO(
                    entity.getAppointmentId(), entity.getStatus(), entity.getSessionDate(),
                    entity.getTimeSlot(), entity.getPatient().getId(), entity.getPatient().getName(),
                    entity.getProgram().getId(), entity.getProgram().getProgramName(),
                    entity.getTherapist().getId(), entity.getTherapist().getName()
            );
        }
        return null;
    }

    @Override
    public boolean isSlotBookedForUpdate(String therapistId, LocalDate date, String time, String apptId) throws Exception {
        return therapySessionDAO.isSlotBookedForUpdate(therapistId, date, time, apptId);
    }

    @Override
    public boolean updateSession(TherapySessionDTO dto) {
        TherapySession entity = therapySessionDAO.search(dto.getAppointmentId());

        if (entity != null) {

            entity.setStatus(dto.getStatus());
            entity.setSessionDate(dto.getSessionDate());
            entity.setTimeSlot(dto.getTimeSlot());


            entity.setPatient(patientDAO.search(dto.getPatientId()));
            entity.setProgram(programDAO.search(dto.getProgramId()));
            entity.setTherapist(therapistDAO.search(dto.getTherapistId()));

            return therapySessionDAO.update(entity);
        }
        return false;
    }

    @Override
    public boolean deleteSession(String appointmentId) throws Exception {
        return therapySessionDAO.delete(appointmentId);
    }

    public Therapist getTherapistDetails(String name) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return therapySessionDAO.getTherapistByName(name, session);
        }
    }


    public List<TherapyScheduleTM> getTodayDoctorSchedule(String therapistId) throws Exception {
        List<TherapyScheduleTM> tmList = new ArrayList<>();

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<TherapySession> entityList = therapySessionDAO.getTodaySessionsByTherapist(therapistId, session);

            for (TherapySession entity : entityList) {
                tmList.add(new TherapyScheduleTM(
                        entity.getTimeSlot(),
                        entity.getPatient().getName(),
                        entity.getStatus()
                ));
            }
        }
        return tmList;
    }

    @Override
    public List<String> getAllSessionId() throws Exception {
        return therapySessionDAO.getAllSessionIds();
    }

    @Override
    public PaymentDetailsDTO getSessionDetailsForPayment(String id) throws Exception {
        TherapySession session = therapySessionDAO.loadDetails(id);

        if (session != null) {

            return new PaymentDetailsDTO(
                    session.getPatient().getName(),
                    session.getTherapist().getName(),
                    session.getProgram().getFee()
            );
        }
        return null;
    }

}

