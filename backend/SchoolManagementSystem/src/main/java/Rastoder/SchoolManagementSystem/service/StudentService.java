package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
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

    private final StudentRepository studentRepository ;
    private final ParentRepository parentRepository;

    public StudentService(StudentRepository studentRepository,ParentRepository parentRepository) {
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


        Set<UUID> savedParentIds = saved.getParents().stream()
                .map(parent -> parent.getParentId())
                .collect(Collectors.toSet());


        return new StudentResponse(
                saved.getStudentId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getBirthDate(),
                saved.getLevel(),
                saved.getLanguage(),
                savedParentIds
        );
    }


    public List<StudentResponse> getAllStudents() {
        List<StudentResponse> responses = studentRepository.findAll().stream().map(student ->
                new StudentResponse(student.getStudentId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getBirthDate(),
                        student.getLevel(),
                        student.getLanguage(),
                        student.getParents().stream().map(parent -> parent.getParentId())
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
        return responses;
    }
}
