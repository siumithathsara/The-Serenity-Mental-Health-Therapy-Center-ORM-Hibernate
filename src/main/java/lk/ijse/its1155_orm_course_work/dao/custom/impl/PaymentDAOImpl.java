package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.PaymentDAO;
import lk.ijse.its1155_orm_course_work.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment entity) {
        return false;
    }

    @Override
    public boolean update(Payment entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Payment search(String id) {
        return null;
    }

    @Override
    public List<Payment> getAll() {
        return List.of();
    }

    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            String sql = "SELECT invoiceNo FROM payment ORDER BY invoiceNo DESC LIMIT 1";

            String lastId = session.createNativeQuery(sql, String.class)
                    .uniqueResult();

            transaction.commit();

            if (lastId != null) {
                int number = Integer.parseInt(lastId.substring(4));
                number++;
                return String.format("I%03d", number);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return "I001";
    }

}
