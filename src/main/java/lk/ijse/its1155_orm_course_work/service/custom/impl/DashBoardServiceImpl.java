package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.DashBoardDAO;
import lk.ijse.its1155_orm_course_work.service.custom.DashBoardService;

import java.sql.SQLException;

public class DashBoardServiceImpl implements DashBoardService {
    private final DashBoardDAO dashboardDAO = (DashBoardDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.DASHBOARD);

    @Override
    public long getPatientCount() throws SQLException {
        return dashboardDAO.getPatientCount();
    }

    @Override
    public long getTodaySessionCount() throws SQLException {
        return dashboardDAO.getTodaySessionCount();
    }

    @Override
    public Double getMonthlyRevenue() throws SQLException {
        return dashboardDAO.getMonthlyRevenue();
    }
}
