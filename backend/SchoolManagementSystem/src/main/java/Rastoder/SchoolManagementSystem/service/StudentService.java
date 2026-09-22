package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.model.Parent;
import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.repository.ParentRepository;
import Rastoder.SchoolManagementSystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    public StudentService(StudentRepository studentRepository, ParentRepository parentRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
    }

    public StudentResponse createStudent(StudentRequest request) throws EntityNotFoundException {

        Set<Parent> parentSet = new HashSet<>();

        if (request.parentIds() != null && !request.parentIds().isEmpty()) {
            List<Parent> fetchedParentsList = parentRepository.findAllById(request.parentIds());

            if (fetchedParentsList.size() != request.parentIds().size()) {
                throw new EntityNotFoundException("One or more Parent IDs provided do not exist in the database.");
            }

            parentSet.addAll(fetchedParentsList);
        }


        Student s = Student.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthDate(request.birthDate())
                .language(request.language())
                .level(request.level())
                .parents(parentSet) // Passes the safe set (empty or filled)
                .build();


        Student saved = studentRepository.save(s);

        return getStudentResponse(saved);
    }


    public List<StudentResponse> getAllStudents() {
        return studentRepository.findByIsActiveTrue().stream().map(this::getStudentResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student with the id " + id
                        + " not found "));
        return getStudentResponse(s);
    }

    public StudentResponse addStudentLevel(UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student with the id " + id + " not found "));

        s.setLevel(s.getLevel() + 1);

        Student saved = studentRepository.save(s);

        return getStudentResponse(saved);
    }

    public StudentResponse archiveStudentWithId(UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student with the id " + id + " not found "));

        s.setActive(false);

        Student saved = studentRepository.save(s);

        return getStudentResponse(saved);
    }

    public List<StudentResponse> getAllNonActiveStudents() {

        return studentRepository.findByIsActiveFalse().stream().map(this::getStudentResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getAllStudentsWithGroupdId(UUID id) {

        List<Student> allStudents = studentRepository.findActiveStudentByGroupId(id);
        return allStudents.stream().map(this::getStudentResponse).collect(Collectors.toList());

    }

    private StudentResponse getStudentResponse(Student s){

        Set<UUID> parents = s.getParents().stream()
                .map(Parent::getParentId)
                .collect(Collectors.toSet());

        return new StudentResponse(s.getStudentId(),
                s.getFirstName(),
                s.getLastName(),
                s.getBirthDate(),
                s.getLevel(),
                s.getLanguage(),
                parents,
                s.isActive());
    }

    public List<StudentResponse> getAllStudentsWithLanguage(Language language) {
        return studentRepository.findByLanguageAndIsActiveTrue(language)
                .stream().map(this::getStudentResponse).collect(Collectors.toList());
    }
}

