package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientHistoryTM {
    private LocalDate date;
    private String programName;
    private String therapistName;
    private String status;
}
