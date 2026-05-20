package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.service.custom.TherapySessionService;

import java.time.LocalDate;
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
}
