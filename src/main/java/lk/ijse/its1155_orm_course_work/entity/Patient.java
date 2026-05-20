package lk.ijse.its1155_orm_course_work.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient")
public class Patient {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nic;

    @Column(unique = true, nullable = false)
    private String phone;

    private String address;

    private LocalDate registeredDate;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TherapySession> therapySessions;

    public Patient(String name, String nic, String phone, String address) {
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.address = address;
    }

    public Patient(String id, String name, String nic, String phone, String address, LocalDate registeredDate) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.address = address;
        this.registeredDate = registeredDate;
    }
}
