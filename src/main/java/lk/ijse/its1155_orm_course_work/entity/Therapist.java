package lk.ijse.its1155_orm_course_work.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "therapist")
public class Therapist {

        @Id
        @Column(name = "therapist_id", length = 10)
        private String id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "specialization")
        private String specialization;

        @Column(name = "status")
        private String status;

        @Column(name = "phone", length = 15)
        private String phone;

        @Column(name = "email")
        private String email;

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "therapist_working_days", joinColumns = @JoinColumn(name = "therapist_id"))
        @Column(name = "working_day")
        private List<String> workingDays = new ArrayList<>();

        @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @ToString.Exclude
        private List<TherapySession> therapySessions;

    public Therapist(String id, String name, String specialization, String status, String phone, String email, List<String> workingDays) {
                                this.id = id;
                                this.name = name;
                                this.specialization = specialization;
                                this.status = status;
                                this.phone = phone;
                                this.email = email;
                                this.workingDays = workingDays;
                                this.therapySessions = new ArrayList<>();
    }
}

