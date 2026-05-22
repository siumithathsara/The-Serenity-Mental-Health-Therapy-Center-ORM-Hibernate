package lk.ijse.its1155_orm_course_work.service.custom.impl;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.ReportDAO;
import lk.ijse.its1155_orm_course_work.dto.tm.FinancialTM;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapistPerformanceTM;
import lk.ijse.its1155_orm_course_work.service.ServiceFactory;
import lk.ijse.its1155_orm_course_work.service.custom.ReportService;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportServiceImpl implements ReportService {

    private final ReportDAO reportDAO = (ReportDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.REPORT);
    @Override
    public List<FinancialTM> getFinancialReport(int month) {
        List<Object[]> rawData = reportDAO.getPaymentsByMonth(month);
        List<FinancialTM> tmList = new ArrayList<>();

        for (Object[] row : rawData) {
            tmList.add(new FinancialTM(
                    row[0].toString(),
                    row[1].toString(),
                    Double.parseDouble(row[2].toString()),
                    row[3].toString()
            ));
        }
        return tmList;
    }

    @Override
    public List<PieChart.Data> getPieChartData(int month) {
        List<Object[]> rawData = reportDAO.getPaymentsByMonth(month);
        Map<String, Double> summaryMap = new HashMap<>();
        List<PieChart.Data> pieData = new ArrayList<>();

        for (Object[] row : rawData) {
            String method = row[3].toString();
            double amount = Double.parseDouble(row[2].toString());
            summaryMap.put(method, summaryMap.getOrDefault(method, 0.0) + amount);
        }

        summaryMap.forEach((method, total) -> pieData.add(new PieChart.Data(method, total)));
        return pieData;
    }

    @Override
    public XYChart.Series<String, Number> getBarChartData() {
        List<Object[]> rawData = reportDAO.getMonthlySessionCount();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sessions (2026)");

        for (Object[] row : rawData) {
            int monthNum = Integer.parseInt(row[0].toString());
            long count = Long.parseLong(row[1].toString());

            String monthName = Month.of(monthNum).name().substring(0, 3);
            series.getData().add(new XYChart.Data<>(monthName, count));
        }
        return series;
    }

    @Override
    public List<TherapistPerformanceTM> getTherapistPerformanceReport() {
        List<Object[]> rawData = reportDAO.getTherapistPerformance();
        List<TherapistPerformanceTM> tmList = new ArrayList<>();

        for (Object[] row : rawData) {
            // row[0] = Name, row[1] = Count

            tmList.add(new TherapistPerformanceTM(
                    row[0].toString(),
                    Integer.parseInt(row[1].toString()),
                    0.0,
                    0.0
            ));
        }
        return tmList;
    }
}
