package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapyProgramDTO {
    private String id;
    private String programName;
    private String duration;
    private double fee;
    private String description;

    public TherapyProgramDTO(String id, String programName, String duration, double fee) {
        this.id = id;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }
}
