package lk.ijse.its1155_orm_course_work.dao.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.custom.ReportDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ReportDAOImpl implements ReportDAO {
    @Override
    public List<Object[]> getPaymentsByMonth(int month) {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {

            String hql = "SELECT p.date, p.therapySession.patient.name, p.amount, p.method FROM Payment p WHERE MONTH(p.date) = :month AND YEAR(p.date) = 2026";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("month", month);
            return query.list();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public List<Object[]> getMonthlySessionCount() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = "SELECT FUNCTION('MONTH', s.sessionDate), COUNT(s.id) FROM TherapySession s " +
                    "WHERE FUNCTION('YEAR', s.sessionDate) = 2026 " +
                    "GROUP BY FUNCTION('MONTH', s.sessionDate)";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public List<Object[]> getTherapistPerformance() {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {

            String hql = "SELECT t.name, COUNT(s.id) FROM TherapySession s JOIN s.therapist t GROUP BY t.id, t.name";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

}
