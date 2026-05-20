package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapyScheduleTM {

    private String timeSlot;
    private String patientName;
    private String status;
}
