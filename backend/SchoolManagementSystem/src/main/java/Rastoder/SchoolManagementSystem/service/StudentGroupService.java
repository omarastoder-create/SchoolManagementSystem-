package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentGroupService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentGroupRepository studentGroupRepository;


    public StudentGroupService(StudentRepository studentRepository, TeacherRepository teacherRepository, StudentGroupRepository studentGroupRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    public StudentGroupResponse createStudentGroup(StudentGroupRequest request){

        Teacher teacher = null;

        if (request.teacherId() != null){
            teacher = teacherRepository.getReferenceById(request.teacherId());
        }

        StudentGroup studentGroup   = StudentGroup.builder()
                .room(request.room())
                .teacher(teacher)
                .build();

        StudentGroup saved = studentGroupRepository.save(studentGroup);

        Set<UUID> sessionsIds = saved.getSessions() != null
                ? saved.getSessions().stream()
                .map(session -> session.getSessionId())
                .collect(Collectors.toSet())
                : new HashSet<>();

        Set<UUID> studentsIds = saved.getStudents() != null
                ? saved.getStudents().stream()
                .map(student -> student.getStudentId())
                .collect(Collectors.toSet())
                : new HashSet<>();

        return new StudentGroupResponse(
                saved.getGroupId(),
                saved.getRoom() ,
                sessionsIds,
                saved.getTeacher() != null ? saved.getTeacher().getTeacherId() : null,
                studentsIds
        );

    }

    public void addStudentToStudentGroup(UUID studentId,UUID studentGroupId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        StudentGroup group = studentGroupRepository.findById(studentGroupId).orElseThrow(EntityNotFoundException::new);

        group.getStudents().add(student);
        studentGroupRepository.save(group);
    }
}
