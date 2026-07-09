package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundExceptions;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITeacherService {

    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO)
        throws EntityAlreadyExistsException, EntityNotFoundExceptions;

    public boolean isTeacherExists(String uuid);

    Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable);
    Page<TeacherReadOnlyDTO> getPaginatedTeachersDeletedFalse(Pageable pageable);

    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO)
        throws EntityNotFoundExceptions, EntityAlreadyExistsException, EntityInvalidArgumentException;

    TeacherEditDTO getTeacherByUUID(UUID uuid) throws EntityNotFoundExceptions;
    public TeacherEditDTO getTeacherByUUIDDeletedFalse(UUID uuid) throws EntityNotFoundExceptions;

    TeacherReadOnlyDTO deleteTeachersByUUID(UUID uuid) throws EntityNotFoundExceptions;


    TeacherReadOnlyDTO deleteTeacherByUUID(UUID uuid);
}
