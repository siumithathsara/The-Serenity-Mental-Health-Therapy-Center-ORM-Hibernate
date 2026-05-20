package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistTM {
    private String id;
    private String name;
    private String specialization;
    private String workingDays;
    private String status;
    private String phone;
}
