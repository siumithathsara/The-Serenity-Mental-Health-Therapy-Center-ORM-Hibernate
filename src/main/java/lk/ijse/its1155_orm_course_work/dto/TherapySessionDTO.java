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
    private String therapistId;
}
