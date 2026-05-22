package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentTM {
    private String invoiceNo;
    private String appId;
    private String patientName;
    private double amount;
    private String method;
    private String status;
}
