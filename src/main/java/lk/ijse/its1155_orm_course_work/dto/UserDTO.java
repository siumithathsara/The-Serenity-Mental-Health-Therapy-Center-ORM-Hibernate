package lk.ijse.its1155_orm_course_work.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private String user_id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;

}
