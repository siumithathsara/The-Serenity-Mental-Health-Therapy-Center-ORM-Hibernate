package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;
import lk.ijse.its1155_orm_course_work.dao.SuperDAO;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import org.hibernate.Session;

import java.util.List;

public interface PatientDAO extends CrudDAO<Patient> {
    public Patient getPatientByName(String name, Session session);
    public List<TherapySession> getPatientTreatmentHistory(String patientId, Session session);

}
