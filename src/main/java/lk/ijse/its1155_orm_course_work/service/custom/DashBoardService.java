package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.sql.SQLException;

public interface DashBoardService extends SuperService {

    long getPatientCount() throws SQLException;
    long getTodaySessionCount()throws SQLException;
    Double getMonthlyRevenue() throws SQLException;
}
