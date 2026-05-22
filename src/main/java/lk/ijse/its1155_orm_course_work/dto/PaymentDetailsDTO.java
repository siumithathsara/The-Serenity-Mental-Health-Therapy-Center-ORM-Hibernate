package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDetailsDTO {

    private String patientName;
    private String therapistName;
    private double programFee;
}
