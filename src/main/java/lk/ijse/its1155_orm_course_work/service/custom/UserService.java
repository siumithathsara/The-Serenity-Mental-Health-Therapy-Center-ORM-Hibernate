package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.service.SuperService;

public interface UserService extends SuperService {

    String authenticateUser(String username, String password) throws Exception;

}
