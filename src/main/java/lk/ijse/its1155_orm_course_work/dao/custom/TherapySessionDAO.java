package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;

import java.time.LocalDate;
import java.util.List;

public interface TherapySessionDAO extends CrudDAO<TherapySession> {

    public List<String> getBookedTimeSlots(String therapistId, LocalDate date) throws Exception;
}
