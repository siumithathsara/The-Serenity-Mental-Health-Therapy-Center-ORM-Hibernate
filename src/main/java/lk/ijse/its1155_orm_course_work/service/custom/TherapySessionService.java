package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.TherapySessionDTO;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface TherapySessionService extends SuperService {
    public List<String> getBookedTimeSlots(String therapistId, LocalDate selectedDate) throws Exception;

    public String generateNextCustomerId() throws Exception;

    public boolean bookSession(TherapySessionDTO sessionDTO) throws  Exception;

    public boolean isSlotBooked(String therapistId, LocalDate sessionDate, String cleanTimeSlot);

    public List<TherapySessionDTO> getAllSession() throws Exception;
}
