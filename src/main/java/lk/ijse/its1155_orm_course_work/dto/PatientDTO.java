package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientDTO {

    private String id;
    private String name;
    private String nic;
    private String phone;
    private String address;
    private LocalDate registeredDate;


    public PatientDTO(String name, String nic, String phone, String address, LocalDate registeredDate) {
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.address = address;
        this.registeredDate = registeredDate;
    }
}
