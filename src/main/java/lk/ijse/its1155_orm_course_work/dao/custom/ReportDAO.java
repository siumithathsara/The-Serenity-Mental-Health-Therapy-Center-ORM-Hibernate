package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.SuperDAO;

import java.util.List;

public interface ReportDAO extends SuperDAO {
    List<Object[]> getPaymentsByMonth(int month);
    List<Object[]> getMonthlySessionCount();
    List<Object[]> getTherapistPerformance();

}
