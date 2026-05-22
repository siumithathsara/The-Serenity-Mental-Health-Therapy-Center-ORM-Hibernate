package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface TherapistService extends SuperService {

    public boolean saveTherapist(TherapistDTO therapistDTO) throws Exception;

    public String generateNextCustomerId() throws Exception, ClassNotFoundException, SQLException;

    public List<TherapistDTO> getAllTherapists(String programId) throws Exception;

    public TherapistDTO searchTherapist(String id) throws Exception;

    public boolean updateTherapist(TherapistDTO therapistDTO) throws Exception;

    public boolean deleteTherapist(String therapistId) throws Exception;
}
