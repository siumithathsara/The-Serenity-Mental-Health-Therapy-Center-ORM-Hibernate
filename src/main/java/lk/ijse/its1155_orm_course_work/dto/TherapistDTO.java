package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistDTO {
    private String id;
    private String name;
    private String specialization;
    private String status;
    private String phone;
    private String email;
    private List<String> workingDays;
}
