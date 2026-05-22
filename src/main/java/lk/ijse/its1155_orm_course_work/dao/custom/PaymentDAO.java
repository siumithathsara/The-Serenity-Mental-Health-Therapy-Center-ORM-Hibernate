package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;
import lk.ijse.its1155_orm_course_work.entity.Payment;
import lk.ijse.its1155_orm_course_work.exception.PaymentProcessingException;
import org.hibernate.Session;

import java.util.List;

public interface PaymentDAO extends CrudDAO<Payment> {
    public boolean save(Payment entity, Session session) throws Exception;

    List<Payment> getAllPayments() throws PaymentProcessingException;

    public List<String> getPendingAppointmentIds();
}
