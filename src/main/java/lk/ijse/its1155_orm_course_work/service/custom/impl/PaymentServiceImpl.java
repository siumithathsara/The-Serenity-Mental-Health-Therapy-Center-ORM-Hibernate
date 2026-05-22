package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.PaymentDAO;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.dto.PaymentDTO;
import lk.ijse.its1155_orm_course_work.entity.Payment;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import lk.ijse.its1155_orm_course_work.exception.PaymentProcessingException;
import lk.ijse.its1155_orm_course_work.service.custom.PaymentService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;

public class PaymentServiceImpl implements PaymentService {
    private final TherapySessionDAO therapySessionDAO =(TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_SESSION);
    private final PaymentDAO paymentDAO =(PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    @Override
    public String generateNextPaymentId() throws PaymentProcessingException, SQLException {
        return paymentDAO.generateNextId();
    }

    @Override
    public boolean savePaymentAndConfirmSession(PaymentDTO paymentDTO) throws PaymentProcessingException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();


            TherapySession therapySession = therapySessionDAO.search(paymentDTO.getSessionId(), session);

            if (therapySession == null) {

                throw new PaymentProcessingException("Therapy Session ID not found " + paymentDTO.getSessionId());
            }

            Payment payment = new Payment();
            payment.setInvoiceNo(paymentDTO.getInvoiceNo());
            payment.setAmount(paymentDTO.getAmount());
            payment.setDate(paymentDTO.getDate());
            payment.setMethod(paymentDTO.getMethod());
            payment.setStatus("Paid");
            payment.setTherapySession(therapySession);

            paymentDAO.save(payment, session);

            therapySession.setStatus("CONFIRMED");
            therapySessionDAO.update(therapySession, session);

            transaction.commit();
            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();

            throw new PaymentProcessingException("Payment Unsuccess " + e.getMessage());
        } finally {
            session.close();
        }
    }

}
