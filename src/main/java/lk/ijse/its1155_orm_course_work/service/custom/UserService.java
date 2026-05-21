package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.UserDTO;
import lk.ijse.its1155_orm_course_work.entity.User;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.util.ArrayList;

public interface UserService extends SuperService {

    String authenticateUser(String username, String password) throws Exception;
    public boolean saveUser(UserDTO userDTO) throws Exception;
    ArrayList<UserDTO> getAllUsers() ;
    boolean updateUser(UserDTO userDTO);
    String generateNextUserId();
    User searchUserByUsername(String searchUsername);
    boolean deleteUser(String userId);
}
