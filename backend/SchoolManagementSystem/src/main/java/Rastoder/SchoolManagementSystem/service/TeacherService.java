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
                savedTeacher.getLanguages(),
                savedTeacher.isActive()
                // todo refactor the method to return TeacherResponse
        );

    }

    public List<TeacherResponse> getAllTeachers() {

        return teacherRepository.findAll().stream().map(teacher -> new TeacherResponse(
                teacher.getTeacherId(),
                teacher.getFirstName(),
                teacher.getFamilyName(),
                teacher.getPhoneNumber(),
                teacher.getDescription(),
                teacher.getLanguages(),
                teacher.isActive()
        )).collect(Collectors.toList());
    }

    public TeacherResponse getTeacherById(UUID id) {
        Teacher te = teacherRepository.findById(id).orElseThrow(()->
                new RuntimeException("The UUID doesn't match a Teacher."));

        return new TeacherResponse(te.getTeacherId(),
                te.getFirstName(),
                te.getFamilyName(),
                te.getPhoneNumber(),
                te.getDescription(),
                te.getLanguages(),
                te.isActive());
    }

    public TeacherResponse updateTeacherWithId(UUID id, TeacherRequest request) {
        Teacher te = teacherRepository.findById(id).orElseThrow(()->
                new RuntimeException("The UUID doesn't match a Teacher."));

        // 2. Mutate (The Smart Update)
        if (request.description() != null) {
            te.setDescription(request.description());
        }

        if (request.languages() != null) {
            te.setLanguages(request.languages());
        }

        if (request.phoneNumber() != null) {
            te.setPhoneNumber(request.phoneNumber());
        }


        Teacher savedTeacher = teacherRepository.save(te);

        return new TeacherResponse(
                savedTeacher.getTeacherId(),
                savedTeacher.getFirstName(),
                savedTeacher.getFamilyName(),
                savedTeacher.getPhoneNumber(),
                savedTeacher.getDescription(),
                savedTeacher.getLanguages(),
                savedTeacher.isActive()
        );
    }
}
