package lk.ijse.its1155_orm_course_work.dto.tm;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserTM {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String role;
}
