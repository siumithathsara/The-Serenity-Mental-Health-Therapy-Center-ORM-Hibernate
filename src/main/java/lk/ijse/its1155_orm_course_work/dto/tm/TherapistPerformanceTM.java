package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapistPerformanceTM {

    private String therapistName;
    private int totalSessions;
    private double avgRating;
    private double noShowPercentage;
}
