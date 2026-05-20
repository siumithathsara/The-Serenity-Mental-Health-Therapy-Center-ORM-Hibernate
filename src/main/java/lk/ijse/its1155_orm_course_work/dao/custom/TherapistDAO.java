package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import org.hibernate.Session;

import java.util.List;

public interface TherapistDAO extends CrudDAO<Therapist> {
    public List<Therapist> getAllTherapists(String programValue, Session session) throws Exception;
}
