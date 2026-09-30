package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;


    public TeacherService(TeacherRepository teacherRepository) {

        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request){
        Teacher t = Teacher.builder()
                .firstName(request.firstName())
                .familyName(request.familyName())
                .phoneNumber(request.phoneNumber())
                .description(request.description())
                .languages(request.languages())
                .build();

        Teacher savedTeacher = teacherRepository.save(t);

        return getTeacherResponse(savedTeacher);

    }

    public List<TeacherResponse> getAllActiveTeachers() {

        return teacherRepository.findByIsActiveTrue().stream()
                .map(this::getTeacherResponse).collect(Collectors.toList());
    }

    public TeacherResponse getTeacherById(UUID id) {
        Teacher te = teacherRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("The UUID doesn't match a Teacher."));

        return getTeacherResponse(te);
    }

    @Transactional
    public TeacherResponse updateTeacherWithId(UUID id, TeacherRequest request) {
        Teacher te = teacherRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("The UUID doesn't match a Teacher."));

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

        return getTeacherResponse(savedTeacher);
    }

    @Transactional
    public TeacherResponse updateTeacherToInactive(UUID id) {
        Teacher te = teacherRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("The UUID doesn't match a Teacher."));

        te.setActive(false);
        Teacher saved = teacherRepository.save(te);

        return getTeacherResponse(saved);
    }

    private TeacherResponse getTeacherResponse(Teacher te){
        return new TeacherResponse(te.getTeacherId(),
                te.getFirstName(),
                te.getFamilyName(),
                te.getPhoneNumber(),
                te.getDescription(),
                te.getLanguages(),
                te.isActive());
    }

    public List<TeacherResponse> getAllInactiveTeachers() {
        return teacherRepository.findByIsActiveFalse().stream()
                .map(this::getTeacherResponse).collect(Collectors.toList());
    }
}
