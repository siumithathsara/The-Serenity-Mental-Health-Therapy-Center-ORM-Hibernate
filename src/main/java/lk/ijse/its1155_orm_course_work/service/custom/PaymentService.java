package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.PaymentDTO;
import lk.ijse.its1155_orm_course_work.exception.PaymentProcessingException;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.sql.SQLException;

public interface PaymentService extends SuperService {

    String generateNextPaymentId() throws PaymentProcessingException , SQLException ;

    boolean savePaymentAndConfirmSession(PaymentDTO paymentDTO)throws PaymentProcessingException;
}
