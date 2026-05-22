package lk.ijse.its1155_orm_course_work.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "therapy_session")
public class TherapySession {
    @Id
    @Column(name = "appointment_id", length = 20)
    private String appointmentId;

    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "time_slot", length = 50, nullable = false)
    private String timeSlot;

    // Foreign Key Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id", nullable = false)
    private TherapyProgram program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    
}
