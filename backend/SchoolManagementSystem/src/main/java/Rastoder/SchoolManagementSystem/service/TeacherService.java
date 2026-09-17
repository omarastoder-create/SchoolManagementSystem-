package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;


    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public TeacherResponse createTeacher(TeacherRequest request){
        Teacher t = Teacher.builder()
                .firstName(request.firstName())
                .familyName(request.familyName())
                .phoneNumber(request.phoneNumber())
                .description(request.description())
                .languages(request.languages())
                .build();

        Teacher savedTeacher = teacherRepository.save(t);

        return new TeacherResponse(
                savedTeacher.getTeacherId(),
                savedTeacher.getFirstName(),
                savedTeacher.getFamilyName(),
                savedTeacher.getPhoneNumber(),
                savedTeacher.getDescription(),
                savedTeacher.getLanguages()
        );

    }
}
