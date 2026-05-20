package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.dto.TherapySessionDTO;
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
    private final TherapySessionDAO therapySessionDAO =(TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    @Override
    public List<String> getBookedTimeSlots(String therapistId, LocalDate selectedDate) throws Exception {
        return therapySessionDAO.getBookedTimeSlots(therapistId, selectedDate);
    }

    @Override
    public String generateNextCustomerId() throws Exception {
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

            // 3. Entity එක හදලා සේව් කරන්න
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

}

