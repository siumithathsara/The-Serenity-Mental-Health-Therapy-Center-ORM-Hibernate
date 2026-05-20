package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
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
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            String hql = "SELECT t.id FROM TherapySession t ORDER BY t.id DESC";

            String lastId = session.createQuery(hql, String.class)
                    .setMaxResults(1)
                    .uniqueResult();

            transaction.commit();

            if (lastId != null) {

                int number = Integer.parseInt(lastId.substring(1));
                number++;
                return String.format("A%03d", number);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return "A001";
    }


    public List<String> getBookedTimeSlots(String therapistId, LocalDate date) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String hql = "SELECT timeSlot FROM TherapySession WHERE id = :therapistId AND sessionDate = :date AND status != 'CANCELLED'";

        Query query = session.createQuery(hql);
        query.setParameter("therapistId", therapistId);
        query.setParameter("date", date);

        List<String> bookedSlots = query.list();

        transaction.commit();
        session.close();

        return bookedSlots;
    }
}
