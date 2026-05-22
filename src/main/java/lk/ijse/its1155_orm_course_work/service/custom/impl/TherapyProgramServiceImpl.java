package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapyProgramDAO;
import lk.ijse.its1155_orm_course_work.dto.TherapyProgramDTO;
import lk.ijse.its1155_orm_course_work.entity.TherapyProgram;
import lk.ijse.its1155_orm_course_work.service.custom.TherapyProgramingService;

import java.util.ArrayList;
import java.util.List;

public class TherapyProgramServiceImpl implements TherapyProgramingService {
    private TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPY_PROGRAM);

    @Override
    public boolean saveProgram(TherapyProgramDTO programDTO) throws Exception {
        return programDAO.save(new TherapyProgram(
                programDTO.getId(),
                programDTO.getProgramName(),
                programDTO.getDuration(),
                programDTO.getFee(),
                programDTO.getDescription()
        ));
    }

    @Override
    public String generateNextCustomerId() throws Exception, ClassNotFoundException {
        return programDAO.generateNextId();
    }

    @Override
    public List<TherapyProgramDTO> getPrograms() throws Exception, ClassNotFoundException {
        List<TherapyProgram> programs = programDAO.getAll();
        List<TherapyProgramDTO> programDTOs = new ArrayList<>();
        for (TherapyProgram program : programs) {
            TherapyProgramDTO programDTO = new TherapyProgramDTO(
                    program.getId(),
                    program.getProgramName(),
                    program.getDuration(),
                    program.getFee());

            programDTOs.add(programDTO);

        }
        return programDTOs;
    }

    @Override
    public TherapyProgramDTO searchProgram(String id) throws Exception {
        TherapyProgram therapyProgram = programDAO.search(id);

        if (therapyProgram == null) {
            return null;
        }

        return new TherapyProgramDTO(
                therapyProgram.getId(),
                therapyProgram.getProgramName(),
                therapyProgram.getDuration(),
                therapyProgram.getFee(),
                therapyProgram.getDescription()

        );
    }

    @Override
    public boolean updateProgram(TherapyProgramDTO programDTO) throws Exception {
        return programDAO.update(new TherapyProgram(
                programDTO.getId(),
                programDTO.getProgramName(),
                programDTO.getDuration(),
                programDTO.getFee(),
                programDTO.getDescription()
        ));
    }

    @Override
    public boolean deleteCustomer(String programId) throws Exception {
        return programDAO.delete(programId);
    }
}
