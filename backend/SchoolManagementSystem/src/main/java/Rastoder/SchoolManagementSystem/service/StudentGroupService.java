package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.exception.BusinessRuleViolationException;
import Rastoder.SchoolManagementSystem.model.*;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import Rastoder.SchoolManagementSystem.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    final int MAX_GROUP_CAPACITY = 20;


    public StudentGroupService(StudentRepository studentRepository, TeacherRepository teacherRepository, StudentGroupRepository studentGroupRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @Transactional
    public StudentGroupResponse createStudentGroup(StudentGroupRequest request) {
        if (request.studentsIds() != null && request.studentsIds().size()>MAX_GROUP_CAPACITY){
            throw new BusinessRuleViolationException("Group capacity can not exceed "+MAX_GROUP_CAPACITY+ " students.");
        }

        Teacher teacher = null;
        if (request.teacherId() != null) {
            teacher = teacherRepository.findById(request.teacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id:"+ request.teacherId()));
        }

        Set<Student> students = new HashSet<>();
        if (request.studentsIds() != null && !request.studentsIds().isEmpty()) {
            students.addAll(studentRepository.findAllById(request.studentsIds()));
            if (students.size() != request.studentsIds().size()) {
                throw new EntityNotFoundException("One or more students not found for the provided IDs");
            }
        }
        validateGroupInvariants(teacher, students);


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

    private void validateGroupInvariants(Teacher teacher, Set<Student> students) {
        if (students == null || students.isEmpty()) {
            return;
        }

        if (students.size() > MAX_GROUP_CAPACITY) {
            throw new BusinessRuleViolationException(
                    "Group capacity cannot exceed " + MAX_GROUP_CAPACITY + " students"
            );
        }

        if (teacher != null) {
            if (teacher.getLanguages() == null || teacher.getLanguages().isEmpty()) {
                throw new BusinessRuleViolationException("Assigned teacher has no languages configured");
            }

            boolean allStudentsMatchTeacher = students.stream()
                    .allMatch(student -> student.getLanguage() != null
                            && teacher.getLanguages().contains(student.getLanguage()));

            if (!allStudentsMatchTeacher) {
                throw new BusinessRuleViolationException(
                        "Each student must have the same language as the teacher"
                );
            }
        }
    }

    @Transactional
    public void addStudentToStudentGroup(UUID studentId, UUID studentGroupId) throws EntityNotFoundException {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("Student not Found"));
        StudentGroup group = studentGroupRepository.findById(studentGroupId).orElseThrow(
                () -> new EntityNotFoundException("Group not Found"));

        if (student.getStudentGroup() != null) {
            if (student.getStudentGroup().getGroupId().equals(studentGroupId)) {
                throw new BusinessRuleViolationException("Student is already assigned to this group");
            } else {
                throw new BusinessRuleViolationException("Student is already assigned to another group");
            }
        }
        
        if (group.getStudents().size()>=MAX_GROUP_CAPACITY){
            throw new BusinessRuleViolationException("Group capacity can not exceed "+MAX_GROUP_CAPACITY+ " students.");
        }
        Teacher teacher = group.getTeacher();
        if (teacher != null && (teacher.getLanguages() == null || !teacher.getLanguages().contains(student.getLanguage()))) {
            throw new BusinessRuleViolationException("Each student must have the same language as the teacher");
        }

        group.getStudents().add(student);
        student.setStudentGroup(group);
        studentGroupRepository.save(group);
    }

    public List<StudentGroupResponse> getAllStudentGroups() {
        List<StudentGroup> listOfSG = studentGroupRepository.findAll();

        return listOfSG.stream().map(this::getStudentGroupResponse).collect(Collectors.toList());
    }

    public StudentGroupResponse getStudentGroupById(UUID id) {
        StudentGroup response = studentGroupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("" +
                "StudentGroupId does not exist"));
            return getStudentGroupResponse(response);
    }

    @Transactional
    public StudentGroupResponse assignTeacher(UUID groupId, UUID teacherId) {
        StudentGroup group = studentGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("StudentGroup not found with id: " + groupId));

        Teacher newTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));

        validateGroupInvariants(newTeacher, group.getStudents());

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
