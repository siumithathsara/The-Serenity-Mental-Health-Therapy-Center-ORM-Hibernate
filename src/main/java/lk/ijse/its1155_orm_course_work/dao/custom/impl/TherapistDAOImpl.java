package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapistDAO;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {

    @Override
    public boolean save(Therapist entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }


    @Override
    public boolean update(Therapist entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Therapist therapist = session.get(Therapist.class, id);

            if (therapist != null) {
                session.delete(therapist);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Therapist search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Therapist therapist = session.get(Therapist.class, id);

            if (therapist != null && therapist.getWorkingDays() != null) {
                therapist.getWorkingDays().size();
            }

            transaction.commit();
            return therapist;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Therapist> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {

            Query<Therapist> query = session.createQuery("FROM Therapist", Therapist.class);
            List<Therapist> list = query.list();

            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }



    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            String sql = "SELECT therapist_id FROM therapist ORDER BY therapist_id DESC LIMIT 1";

            String lastId = session.createNativeQuery(sql, String.class)
                    .uniqueResult();

            transaction.commit();


            if (lastId != null) {

                int number = Integer.parseInt(lastId.substring(1));
                number++;

                return String.format("T%03d", number);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return "T001";
    }

    @Override
    public List<Therapist> getAllTherapists(String programValue, Session session) throws Exception {
        if (programValue == null || programValue.isBlank()) {

            return session.createQuery("FROM Therapist", Therapist.class).list();
        } else {

            return session.createQuery("FROM Therapist WHERE specialization = :progVal AND status = 'Active'", Therapist.class)
                    .setParameter("progVal", programValue)
                    .list();
        }
    }
}
