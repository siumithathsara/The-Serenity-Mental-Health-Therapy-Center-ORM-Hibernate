package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface TherapySessionService extends SuperService {
    public List<String> getBookedTimeSlots(String therapistId, LocalDate selectedDate) throws Exception;

    public String generateNextCustomerId() throws Exception;
}
