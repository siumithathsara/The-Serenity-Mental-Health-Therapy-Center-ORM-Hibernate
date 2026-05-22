package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.PatientDAO;
import lk.ijse.its1155_orm_course_work.dto.PatientDTO;
import lk.ijse.its1155_orm_course_work.dto.tm.PatientHistoryTM;
import lk.ijse.its1155_orm_course_work.entity.Patient;
import lk.ijse.its1155_orm_course_work.entity.TherapySession;
import lk.ijse.its1155_orm_course_work.service.custom.PatientService;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientServiceImpl implements PatientService {
    private PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public boolean savePatient(PatientDTO patientDTO) throws Exception {
        return patientDAO.save(new Patient(
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getNic(),
                patientDTO.getPhone(),
                patientDTO.getAddress(),
                patientDTO.getRegisteredDate()
        ));
    }

    @Override
    public String generateNextCustomerId() throws Exception {
        return patientDAO.generateNextId();
    }

    @Override
    public List<PatientDTO> getPatients() throws SQLException, ClassNotFoundException {
        List<Patient> patients = patientDAO.getAll();
        List<PatientDTO> patientDTOs = new ArrayList<>();
        for (Patient patient : patients) {
            PatientDTO patientDTO = new PatientDTO(
                    patient.getId(),
                    patient.getName(),
                    patient.getNic(),
                    patient.getPhone(),
                    patient.getAddress(),
                    patient.getRegisteredDate());

            patientDTOs.add(patientDTO);
        }
        return patientDTOs;
    }

    @Override
    public boolean updatePatient(PatientDTO patientDTO) throws Exception {

        return patientDAO.update(new Patient(
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getNic(),
                patientDTO.getPhone(),
                patientDTO.getAddress(),
                patientDTO.getRegisteredDate()
        ));
    }

    @Override
    public PatientDTO searchPatient(String id) throws SQLException, ClassNotFoundException {
        Patient patient = patientDAO.search(id);
        if (patient == null) {
            return null;
        }
        return new PatientDTO(
                patient.getName(),
                patient.getNic(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getRegisteredDate()
        );
    }

    @Override
    public boolean deleteCustomer(String patientId) throws SQLException, ClassNotFoundException {
        return patientDAO.delete(patientId);
    }

    public Patient searchPatientByName(String name) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return patientDAO.getPatientByName(name, session);
        }
    }


    public List<PatientHistoryTM> getPatientHistory(String patientId) throws Exception {
        List<PatientHistoryTM> historyList = new ArrayList<>();

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<TherapySession> sessions = patientDAO.getPatientTreatmentHistory(patientId, session);

            for (TherapySession s : sessions) {
                historyList.add(new PatientHistoryTM(
                        s.getSessionDate(),
                        s.getProgram().getProgramName(),
                        s.getTherapist().getName(),
                        s.getStatus()
                ));
            }
        }
        return historyList;
    }
}
