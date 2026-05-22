package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.DashBoardDAO;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class DashBoardDAOImpl implements DashBoardDAO {

    @Override
    public boolean save(Object entity) {
        return false;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Object search(String id) {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public String generateNextId() {
        return "";
    }

    @Override
    public long getPatientCount() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        long count = (long) session.createQuery("SELECT COUNT(p.id) FROM Patient p").uniqueResult();
        session.close();
        return count;
    }

    @Override
    public long getTodaySessionCount() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        long count = (long) session.createQuery("SELECT COUNT(s.id) FROM TherapySession s WHERE s.sessionDate = CURRENT_DATE").uniqueResult();
        session.close();
        return count;
    }

    @Override
    public Double getMonthlyRevenue() throws SQLException {
        System.out.println("DAO Method reached!");
        Session session = FactoryConfiguration.getInstance().getSession();
        try {

            String hql = "SELECT SUM(p.amount) FROM Payment p WHERE MONTH(p.date) = 5 AND YEAR(p.date) = 2026";
            Object result = session.createQuery(hql).uniqueResult();

            System.out.println("Query Result: " + result);

            return (result != null) ? (Double) result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            session.close();
        }
    }
}
