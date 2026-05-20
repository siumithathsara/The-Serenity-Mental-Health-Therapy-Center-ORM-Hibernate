package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapyProgramDAO;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {
    @Override
    public boolean save(TherapyProgram entity) {
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
    public boolean update(TherapyProgram entity) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
            return true;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            TherapyProgram therapyProgram = session.get(TherapyProgram.class, id);

            if (therapyProgram != null) {

                session.remove(therapyProgram);

                transaction.commit();
                return true;
            }


            return false;

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {

            session.close();
        }
    }

    @Override
    public TherapyProgram search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        TherapyProgram program = null;

        try {
            transaction = session.beginTransaction();

            program = session.get(TherapyProgram.class, id);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return program;
    }

    @Override
    public List<TherapyProgram> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        List<TherapyProgram> programList = new ArrayList<>();

        try {

            String hql = "FROM TherapyProgram t ORDER BY t.id DESC";
            programList = session.createQuery(hql, TherapyProgram.class).getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return programList;
    }

    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        String nextId = "MT001";

        try {

            transaction = session.beginTransaction();


            String hql = "SELECT t.id FROM TherapyProgram t ORDER BY t.id DESC";
            Query<String> query = session.createQuery(hql, String.class);
            query.setMaxResults(1);

            String lastId = query.uniqueResult();


            if (lastId != null) {

                int number = Integer.parseInt(lastId.substring(2));
                number++;

                nextId = String.format("MT%03d", number);
            }


            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {

            session.close();
        }

        return nextId;
    }
}
