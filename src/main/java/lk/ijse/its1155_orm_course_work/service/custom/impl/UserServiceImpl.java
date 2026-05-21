package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.UserDAO;
import lk.ijse.its1155_orm_course_work.dto.UserDTO;
import lk.ijse.its1155_orm_course_work.entity.User;
import lk.ijse.its1155_orm_course_work.service.custom.UserService;
import lk.ijse.its1155_orm_course_work.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean saveUser(UserDTO userDTO) throws Exception {
        User optionalUser = userDAO.findByUsername(userDTO.getUsername());
        if (optionalUser != null) {
            return false;
        }
        User user = new User();
        user.setUser_id(userDTO.getUser_id());
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());

        return userDAO.save(user);
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() {
        List<User> users = userDAO.getAll();

        ArrayList<UserDTO> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDTO userDto = new UserDTO();
            userDto.setUser_id(user.getUser_id());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRole());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        try {
            User optionalUser = userDAO.findByUsername(userDTO.getUsername());
            if (optionalUser == null) {
                return false;
            }

            optionalUser.setUser_id(userDTO.getUser_id());
            optionalUser.setName(userDTO.getName());
            optionalUser.setEmail(userDTO.getEmail());
            optionalUser.setRole(userDTO.getRole());

            if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {

                String newHashedPassword = PasswordUtil.hashPassword(userDTO.getPassword());
                optionalUser.setPassword(newHashedPassword);
            }

            return userDAO.update(optionalUser);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateNextUserId() {
        return userDAO.generateNextId();
    }
}

