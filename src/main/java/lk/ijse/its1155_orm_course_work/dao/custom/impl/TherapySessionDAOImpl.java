package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;

import java.util.List;

public class TherapySessionDAOImpl implements TherapySessionDAO {
    @Override
    public boolean save(TherapySession entity) {
        return false;
    }

    @Override
    public boolean update(TherapySession entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public TherapySession search(String id) {
        return null;
    }

    @Override
    public List<TherapySession> getAll() {
        return List.of();
    }

    @Override
    public String generateNextId() {
        return "";
    }
}
