package lk.ijse.its1155_orm_course_work.service.custom.impl;

import lk.ijse.its1155_orm_course_work.config.FactoryConfiguration;
import lk.ijse.its1155_orm_course_work.dao.DAOFactory;
import lk.ijse.its1155_orm_course_work.dao.custom.TherapistDAO;
import lk.ijse.its1155_orm_course_work.dto.TherapistDTO;
import lk.ijse.its1155_orm_course_work.entity.Therapist;
import lk.ijse.its1155_orm_course_work.service.custom.TherapistService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TherapistServiceImpl implements TherapistService {

    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    @Override
    public boolean saveTherapist(TherapistDTO therapistDTO) throws Exception {


            Session session = FactoryConfiguration.getInstance().getSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();


                Therapist therapist = new Therapist();
                therapist.setId(therapistDTO.getId());
                therapist.setName(therapistDTO.getName());
                therapist.setSpecialization(therapistDTO.getSpecialization());
                therapist.setStatus(therapistDTO.getStatus());
                therapist.setPhone(therapistDTO.getPhone());
                therapist.setEmail(therapistDTO.getEmail());
                therapist.setWorkingDays(therapistDTO.getWorkingDays()); // List<String> එක කෙලින්ම සෙට් කරගත හැක

                boolean isSaved = therapistDAO.save(therapist);

                if (isSaved) {
                    transaction.commit();
                    return true;
                } else {
                    transaction.rollback();
                    return false;
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                throw e;
            } finally {
                session.close();
            }
        }

    @Override
    public String generateNextCustomerId() throws Exception, ClassNotFoundException, SQLException {
        return therapistDAO.generateNextId();
    }

    @Override
    public List<TherapistDTO> getAllTherapists(String programValue) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            List<Therapist> therapistList = therapistDAO.getAllTherapists(programValue, session);

            transaction.commit();


            List<TherapistDTO> dtoList = new ArrayList<>();
            for (Therapist therapist : therapistList) {
                dtoList.add(new TherapistDTO(
                        therapist.getId(),
                        therapist.getName(),
                        therapist.getSpecialization(),
                        therapist.getStatus(),
                        therapist.getPhone(),
                        therapist.getEmail(),
                        therapist.getWorkingDays()
                ));
            }
            return dtoList;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public TherapistDTO searchTherapist(String id) throws Exception {
        Therapist entity = therapistDAO.search(id);
        if (entity != null) {
            return new TherapistDTO(
                    entity.getId(),
                    entity.getName(),
                    entity.getSpecialization(),
                    entity.getStatus(),
                    entity.getPhone(),
                    entity.getEmail(),
                    entity.getWorkingDays()
            );
        }
        return null;
    }

    @Override
    public boolean updateTherapist(TherapistDTO therapistDTO) throws Exception {
        return therapistDAO.update(new Therapist(
                therapistDTO.getId(),
                therapistDTO.getName(),
                therapistDTO.getSpecialization(),
                therapistDTO.getStatus(),
                therapistDTO.getPhone(),
                therapistDTO.getEmail(),
                therapistDTO.getWorkingDays()
        ));
    }

    @Override
    public boolean deleteTherapist(String therapistId) throws Exception {
        return therapistDAO.delete(therapistId);
    }
}

