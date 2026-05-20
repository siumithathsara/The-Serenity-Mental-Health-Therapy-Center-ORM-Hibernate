package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.UserDAO;
import lk.ijse.its1155_orm_course_work.entity.User;
import lk.ijse.its1155_orm_course_work.service.custom.UserService;
import lk.ijse.its1155_orm_course_work.util.PasswordUtil;

public class UserServiceImpl implements UserService {
    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public String authenticateUser(String username, String password) throws Exception {
        User user = userDAO.findByUsername(username);

        if (user != null) {
            boolean isMatched = PasswordUtil.checkPassword(password, user.getPassword());

            if (isMatched) {
                return user.getRole();
            }
        }

        return null;
    }
    }

