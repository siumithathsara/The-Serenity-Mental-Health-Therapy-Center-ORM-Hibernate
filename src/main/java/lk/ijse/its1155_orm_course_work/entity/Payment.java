package lk.ijse.its1155_orm_course_work.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    private String invoiceNo;

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String status;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", referencedColumnName = "appointment_id", unique = true, nullable = false)
    private TherapySession therapySession;
}
