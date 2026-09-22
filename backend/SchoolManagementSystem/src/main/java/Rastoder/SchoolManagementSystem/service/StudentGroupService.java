package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.*;
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
            teacher = teacherRepository.findById(request.teacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        }

        Set<Student> students = new HashSet<>();
        if (request.studentsIds() != null && !request.studentsIds().isEmpty()) {
            students.addAll(studentRepository.findAllById(request.studentsIds()));
        }

        StudentGroup studentGroup = StudentGroup.builder()
                .room(request.room())
                .teacher(teacher)
                .students(students)
                .build();

        StudentGroup savedGroup = studentGroupRepository.save(studentGroup);


        if (!students.isEmpty()) {
            for (Student student : students) {
                student.setStudentGroup(savedGroup);
            }

            studentRepository.saveAll(students);
        }

        return getStudentGroupResponse(savedGroup);
    }

    public void addStudentToStudentGroup(UUID studentId, UUID studentGroupId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        StudentGroup group = studentGroupRepository.findById(studentGroupId).orElseThrow(EntityNotFoundException::new);

        group.getStudents().add(student);
        studentGroupRepository.save(group);
    }

    public List<StudentGroupResponse> getAllStudentGroups() {
        List<StudentGroup> listOfSG = studentGroupRepository.findAll();

        return listOfSG.stream().map(this::getStudentGroupResponse).collect(Collectors.toList());
    }

    public StudentGroupResponse getStudentGroupByService(UUID id) {
        StudentGroup response = studentGroupRepository.findById(id).orElseThrow(() -> new RuntimeException("" +
                "StudentGroupId does not exist"));
            return getStudentGroupResponse(response);
    }


    public StudentGroupResponse assignTeacher(UUID groupId, UUID teacherId) {

        StudentGroup group = studentGroupRepository.findById(groupId).orElseThrow(() ->
                new RuntimeException("Group not found"));

        Teacher newTeacher = teacherRepository.findById(teacherId).orElseThrow(() ->
                new RuntimeException("Teacher not found"));

        group.setTeacher(newTeacher);

        StudentGroup savedGroup = studentGroupRepository.save(group);
        return getStudentGroupResponse(savedGroup);
    }

    private StudentGroupResponse getStudentGroupResponse(StudentGroup response){
        return new StudentGroupResponse(
                response.getGroupId(),
                response.getRoom(),
                response.getSessions() != null ? response.getSessions().stream().map(
                        Session::getSessionId).collect(Collectors.toSet()):null,
                response.getTeacher() != null ? response.getTeacher().getTeacherId() : null ,
                response.getStudents() != null ? response.getStudents().stream().map(
                        Student::getStudentId).collect(Collectors.toSet()) : new HashSet<>()
        ) ;
    }

    public List<StudentGroupResponse> getStudentGroupByTeacherId(UUID id) {
        return studentGroupRepository.findByTeacher_TeacherId(id)
                .stream().map(this::getStudentGroupResponse).collect(Collectors.toList());
    }
}
