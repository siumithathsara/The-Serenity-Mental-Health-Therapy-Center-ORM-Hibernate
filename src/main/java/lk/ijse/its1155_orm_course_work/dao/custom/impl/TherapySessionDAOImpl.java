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
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        List<TherapySession> list = null;

        try {
            transaction = session.beginTransaction(); // Transaction පටන් ගත්තා

            String hql = "SELECT t FROM TherapySession t " +
                    "JOIN FETCH t.patient " +
                    "JOIN FETCH t.program " +
                    "JOIN FETCH t.therapist";

            list = session.createQuery(hql, TherapySession.class).list();

            transaction.commit(); // Transaction එක commit කළා
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
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
        Transaction transaction = null;
        List<String> bookedSlots = null;

        try {
            transaction = session.beginTransaction();

            String hql = "SELECT t.timeSlot FROM TherapySession t WHERE t.therapist.id = :tId AND t.sessionDate = :date";

            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("tId", therapistId);
            query.setParameter("date", date);

            bookedSlots = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        return bookedSlots;
    }
    @Override
    public boolean save(TherapySession entity, Session session) throws Exception {
        session.persist(entity);
        return true;
    }

    @Override
    public boolean isSlotBooked(String therapistId, LocalDate sessionDate, String timeSlot, Session session) {

        String hql = "SELECT COUNT(t) FROM TherapySession t WHERE t.therapist.id = :tId AND t.sessionDate = :date AND t.timeSlot = :slot";

        Long count = session.createQuery(hql, Long.class)
                .setParameter("tId", therapistId)
                .setParameter("date", sessionDate)
                .setParameter("slot", timeSlot)
                .uniqueResult();

        return count > 0;
    }
}
