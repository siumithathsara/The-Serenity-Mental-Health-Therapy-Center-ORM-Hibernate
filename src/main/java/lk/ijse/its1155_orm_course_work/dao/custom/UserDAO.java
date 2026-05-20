package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;
import lk.ijse.its1155_orm_course_work.dao.SuperDAO;
import lk.ijse.its1155_orm_course_work.entity.User;

public interface UserDAO extends CrudDAO<User> {
    User findByUsername(String username);
}
