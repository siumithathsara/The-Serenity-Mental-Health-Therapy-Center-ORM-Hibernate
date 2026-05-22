package lk.ijse.its1155_orm_course_work.dao.custom;

import lk.ijse.its1155_orm_course_work.dao.CrudDAO;

import java.sql.SQLException;

public interface DashBoardDAO extends CrudDAO {

    long getPatientCount() throws SQLException;

    long getTodaySessionCount() throws SQLException;

    Double getMonthlyRevenue() throws SQLException;

}
