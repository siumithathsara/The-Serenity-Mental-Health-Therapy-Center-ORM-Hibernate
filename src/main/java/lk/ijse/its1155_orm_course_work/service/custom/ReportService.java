package lk.ijse.its1155_orm_course_work.service.custom;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lk.ijse.its1155_orm_course_work.dto.tm.FinancialTM;
import lk.ijse.its1155_orm_course_work.dto.tm.TherapistPerformanceTM;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.util.List;

public interface ReportService extends SuperService {
    public List<FinancialTM> getFinancialReport(int month);

    public List<PieChart.Data> getPieChartData(int month);

    public XYChart.Series<String, Number> getBarChartData();

    public List<TherapistPerformanceTM> getTherapistPerformanceReport();
}
