package lk.ijse.its1155_orm_course_work.service.custom;

import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.service.SuperService;

import java.util.List;

public interface TherapyProgramingService extends SuperService {

    public boolean saveProgram(TherapyProgramDTO programDTO) throws Exception;

    public String generateNextCustomerId() throws Exception, ClassNotFoundException;

    public List<TherapyProgramDTO> getPrograms() throws Exception, ClassNotFoundException;

    public TherapyProgramDTO searchProgram(String id) throws Exception;

    public boolean updateProgram(TherapyProgramDTO programDTO) throws Exception;

    public boolean deleteCustomer(String programId) throws Exception;
}
