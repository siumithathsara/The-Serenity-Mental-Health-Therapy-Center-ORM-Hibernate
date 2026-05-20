package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.PatientDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.PatientHistoryTM;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface PatientService extends SuperService {

    public boolean savePatient(PatientDTO patientDTO) throws Exception;
    public String generateNextCustomerId() throws Exception;
    public  List<PatientDTO> getPatients() throws SQLException, ClassNotFoundException;
    public boolean updatePatient(PatientDTO patientDTO) throws Exception;
    public PatientDTO searchPatient(String id) throws SQLException, ClassNotFoundException;
    public boolean deleteCustomer(String patientId) throws SQLException, ClassNotFoundException;
    public Patient searchPatientByName(String name) throws Exception;
    public List<PatientHistoryTM> getPatientHistory(String patientId) throws Exception;
}
