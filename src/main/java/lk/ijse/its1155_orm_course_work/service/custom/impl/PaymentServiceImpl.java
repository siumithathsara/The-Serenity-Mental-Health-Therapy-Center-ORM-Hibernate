package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.PaymentDAO;
import lk.ijse.its1155_orm_course_work.exception.PaymentProcessingException;
import lk.ijse.its1155_orm_course_work.service.custom.PaymentService;

import java.sql.SQLException;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO =(PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);
    @Override
    public String generateNextPaymentId() throws PaymentProcessingException, SQLException {
        return paymentDAO.generateNextId();
    }
}
