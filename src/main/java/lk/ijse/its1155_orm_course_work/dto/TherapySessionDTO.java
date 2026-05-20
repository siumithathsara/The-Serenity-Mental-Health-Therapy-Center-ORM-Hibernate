package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TherapySessionDTO {
    private String appointmentId;
    private String status;
    private LocalDate sessionDate;
    private String timeSlot;
    private String patientId;
    private String patientName;
    private String programId;
    private String programName;
    private String therapistId;
    private String therapistName;

    public TherapySessionDTO(String appointmentId, String status, LocalDate sessionDate, String cleanTimeSlot, String selectedPatientId, String programId, String therapistId) {
                                                this.appointmentId = appointmentId;
                                                this.status = status;
                                                this.sessionDate = sessionDate;
                                                this.timeSlot = cleanTimeSlot;
                                                this.patientId = selectedPatientId;
                                                this.therapistId = therapistId;
                                                this.programId = programId;
    }
}
