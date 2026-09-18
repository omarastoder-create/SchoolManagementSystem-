package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<TeacherResponse> getAllTeachers() {

        return teacherRepository.findAll().stream().map(teacher -> new TeacherResponse(
                teacher.getTeacherId(),
                teacher.getFirstName(),
                teacher.getFamilyName(),
                teacher.getPhoneNumber(),
                teacher.getDescription(),
                teacher.getLanguages()
        )).collect(Collectors.toList());
    }

    public TeacherResponse getTeacherById(UUID id) {
        Teacher te = teacherRepository.getReferenceById(id);

        return new TeacherResponse(te.getTeacherId(),
                te.getFirstName(),
                te.getFamilyName(),
                te.getPhoneNumber(),
                te.getDescription(),
                te.getLanguages());
    }
}
