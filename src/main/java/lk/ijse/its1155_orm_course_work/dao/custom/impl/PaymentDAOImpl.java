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
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "SELECT p.invoiceNo FROM Payment p ORDER BY p.invoiceNo DESC";
            String lastId = session.createQuery(hql, String.class)
                    .setMaxResults(1)
                    .uniqueResult();

            transaction.commit();

            if (lastId != null) {

                int id = Integer.parseInt(lastId.replace("I", ""));
                return String.format("I%03d", (id + 1));
            } else {
                return "I001";
            }

        } catch (Exception e) {

            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(Payment entity, Session session) throws Exception {
            session.save(entity);
            return true;
        }

}
