package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.PatientDAO;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient entity) {
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
    public boolean update(Patient entity) {
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

            Patient patient = session.get(Patient.class, id);

            if (patient != null) {

                session.remove(patient);

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
    public Patient search(String id) {
        Session session = FactoryConfiguration.getInstance().getSession();

        try {

            Patient patient = session.get(Patient.class, id);

            return patient;

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
    public List<Patient> getAll() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        List<Patient> patientList = new ArrayList<>();

        try {

            String hql = "FROM Patient p ORDER BY p.id DESC";
            patientList = session.createQuery(hql, Patient.class).getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return patientList;
    }

    @Override
    public String generateNextId() {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            String sql = "SELECT id FROM patient ORDER BY id DESC LIMIT 1";

            String lastId = session.createNativeQuery(sql, String.class)
                    .uniqueResult();

            transaction.commit();

            if (lastId != null) {
                int number = Integer.parseInt(lastId.substring(1));
                number++;
                return String.format("P%03d", number);
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return "P001";
    }

    public Patient getPatientByName(String name, Session session) {
        String hql = "FROM Patient p WHERE LOWER(TRIM(p.name)) LIKE LOWER(TRIM(:name))";
        List<Patient> list = session.createQuery(hql, Patient.class)
                .setParameter("name", name)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }


    public List<TherapySession> getPatientTreatmentHistory(String patientId, Session session) {
        String hql = "FROM TherapySession s " +
                "JOIN FETCH s.program pr " +
                "JOIN FETCH s.therapist t " +
                "WHERE s.patient.id = :patientId " +
                "ORDER BY s.sessionDate DESC";

        return session.createQuery(hql, TherapySession.class)
                .setParameter("patientId", patientId)
                .getResultList();
    }
}
