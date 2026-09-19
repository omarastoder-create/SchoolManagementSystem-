package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.Session;
import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.model.Teacher;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    public StudentGroupResponse createStudentGroup(StudentGroupRequest request) {

        Teacher teacher = null;

        if (request.teacherId() != null) {
            teacher = teacherRepository.getReferenceById(request.teacherId());
        }

        StudentGroup studentGroup = StudentGroup.builder()
                .room(request.room())
                .teacher(teacher)
                .build();

        StudentGroup saved = studentGroupRepository.save(studentGroup);

        Set<UUID> sessionsIds = saved.getSessions() != null
                ? saved.getSessions().stream()
                .map(Session::getSessionId)
                .collect(Collectors.toSet())
                : new HashSet<>();

        Set<UUID> studentsIds = saved.getStudents() != null
                ? saved.getStudents().stream()
                .map(Student::getStudentId)
                .collect(Collectors.toSet())
                : new HashSet<>();

        return new StudentGroupResponse(
                saved.getGroupId(),
                saved.getRoom(),
                sessionsIds,
                saved.getTeacher() != null ? saved.getTeacher().getTeacherId() : null,
                studentsIds
        );

    }

    public void addStudentToStudentGroup(UUID studentId, UUID studentGroupId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        StudentGroup group = studentGroupRepository.findById(studentGroupId).orElseThrow(EntityNotFoundException::new);

        group.getStudents().add(student);
        studentGroupRepository.save(group);
    }

    public List<StudentGroupResponse> getAllStudentGroups() {
        List<StudentGroup> listOfSG = studentGroupRepository.findAll();

        return listOfSG.stream().map(studentGroup -> {

            // 1. Safely extract Session IDs
            Set<UUID> sessionIds = (studentGroup.getSessions() != null)
                    ? studentGroup.getSessions().stream()
                    .map(Session::getSessionId)
                    .collect(Collectors.toSet())
                    : new HashSet<>();

            // 2. Safely extract Student IDs
            Set<UUID> studentIds = (studentGroup.getStudents() != null)
                    ? studentGroup.getStudents().stream()
                    .map(Student::getStudentId)
                    .collect(Collectors.toSet())
                    : new HashSet<>();

            // 3. Construct the response safely (Make sure the order matches your DTO constructor!)
            return new StudentGroupResponse(
                    studentGroup.getGroupId(),
                    studentGroup.getRoom(),
                    sessionIds,
                    // Safely check if the teacher exists before grabbing the ID
                    studentGroup.getTeacher() != null ? studentGroup.getTeacher().getTeacherId() : null,
                    studentIds

            );

        }).collect(Collectors.toList());
    }
}
