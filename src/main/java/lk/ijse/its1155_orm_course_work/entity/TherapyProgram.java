package lk.ijse.its1155_orm_course_work.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapy_program")
public class TherapyProgram {
    @Id
    @Column(name = "program_id", length = 15)
    private String id;

    @Column(name = "program_name", nullable = false, length = 100)
    private String programName;

    @Column(name = "duration", length = 50)
    private String duration;

    @Column(name = "fee", nullable = false)
    private double fee;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TherapySession> therapySessions;

    public TherapyProgram(String id, String programName, String duration, double fee, String description) {
                            this.id = id;
                            this.programName = programName;
                            this.duration = duration;
                            this.fee = fee;
                            this.description = description;
    }
}
