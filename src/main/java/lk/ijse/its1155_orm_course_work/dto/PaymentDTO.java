package lk.ijse.its1155_orm_course_work.dto;

import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDTO {
    private String invoiceNo;
    private double amount;
    private LocalDate date;
    private String method;
    private String status;
    private String sessionId;

    private String appointmentId;
    private String patientName;
    private TherapySession therapySession;
}
