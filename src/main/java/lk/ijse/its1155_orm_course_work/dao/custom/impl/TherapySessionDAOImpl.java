package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapySessionDAO;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
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
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {

            TherapySession sessionEntity = session.get(TherapySession.class, id);
            if (sessionEntity != null) {
                session.delete(sessionEntity);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapySession search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();

        try {
            String hql = "SELECT t FROM TherapySession t " +
                    "JOIN FETCH t.patient " +
                    "JOIN FETCH t.program " +
                    "JOIN FETCH t.therapist " +
                    "WHERE t.appointmentId = :id";

            TherapySession sessionEntity = session.createQuery(hql, TherapySession.class)
                    .setParameter("id", id)
                    .uniqueResult();

            return sessionEntity;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<TherapySession> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        List<TherapySession> list = null;

        try {
            transaction = session.beginTransaction();

            String hql = "SELECT t FROM TherapySession t " +
                    "JOIN FETCH t.patient " +
                    "JOIN FETCH t.program " +
                    "JOIN FETCH t.therapist";

            list = session.createQuery(hql, TherapySession.class).list();

            transaction.commit();
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

    @Override
    public boolean isSlotBookedForUpdate(String therapistId, LocalDate date, String time, String apptId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "SELECT COUNT(t) FROM TherapySession t " +
                    "WHERE t.therapist.id = :tId " +
                    "AND t.sessionDate = :date " +
                    "AND t.timeSlot = :time " +
                    "AND t.appointmentId != :currentId";

            Long count = (Long) session.createQuery(hql)
                    .setParameter("tId", therapistId)
                    .setParameter("date", date)
                    .setParameter("time", time)
                    .setParameter("currentId", apptId)
                    .uniqueResult();
            return count > 0;
        } finally {
            session.close();
        }
    }

    public Therapist getTherapistByName(String name, Session session) {
        String hql = "FROM Therapist t WHERE LOWER(TRIM(t.name)) LIKE LOWER(TRIM(:name))";

        List<Therapist> list = session.createQuery(hql, Therapist.class)
                .setParameter("name", name)
                .getResultList();

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    public List<TherapySession> getTodaySessionsByTherapist(String therapistId, Session session) {
        String hql = "FROM TherapySession s " +
                "JOIN FETCH s.patient p " +
                "WHERE s.therapist.id = :therapistId " +
                "AND s.sessionDate = :today " +
                "AND s.status != 'Cancelled' " +
                "ORDER BY s.timeSlot ASC";

        return session.createQuery(hql, TherapySession.class)
                .setParameter("therapistId", therapistId)
                .setParameter("today", LocalDate.now())
                .getResultList();
    }

    @Override
    public TherapySession loadDetails(String id) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapySession.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getAllSessionIds() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.createQuery("SELECT s.appointmentId FROM TherapySession s", String.class).list();
        } finally {
            session.close();
        }
    }

    public TherapySession search(String id, Session session) throws Exception {
        return session.get(TherapySession.class, id);
    }

    public boolean update(TherapySession entity, Session session) throws Exception {
        session.update(entity);
        return true;
    }
}
