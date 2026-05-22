package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.PaymentDetailsDTO;
import lk.ijse.its1155_orm_course_work.dto.TherapySessionDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapyScheduleTM;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface TherapySessionService extends SuperService {
    public List<String> getBookedTimeSlots(String therapistId, LocalDate selectedDate) throws Exception;

    public String generateNextSessionId() throws Exception;

    public boolean bookSession(TherapySessionDTO sessionDTO) throws Exception;

    public boolean isSlotBooked(String therapistId, LocalDate sessionDate, String cleanTimeSlot);

    public List<TherapySessionDTO> getAllSession() throws Exception;

    public TherapySessionDTO searchSession(String appointmentId) throws Exception;

    public boolean isSlotBookedForUpdate(String therapistId, LocalDate date, String time, String apptId) throws Exception;

    public boolean updateSession(TherapySessionDTO dto);

    public boolean deleteSession(String appointmentId) throws Exception;

    public Therapist getTherapistDetails(String name) throws Exception;

    public List<TherapyScheduleTM> getTodayDoctorSchedule(String therapistId) throws Exception;

    List<String> getAllSessionId() throws Exception;

    PaymentDetailsDTO getSessionDetailsForPayment(String selectedSessionId) throws Exception;
}
