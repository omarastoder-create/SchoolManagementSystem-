package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.dto.StudentRequest;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.Language;
import Rastoder.SchoolManagementSystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request){
        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable UUID id){
        StudentResponse response = studentService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/addGrade/{id}")
    public
    ResponseEntity<StudentResponse> addLevelToStudent(@PathVariable UUID id){
        StudentResponse response = studentService.addStudentLevel(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponse> archiveStudentWithId(@PathVariable UUID id){
        StudentResponse response = studentService.archiveStudentWithId(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/alumni")
    public ResponseEntity<List<StudentResponse>> getAllNonActiveStudents(){
        List<StudentResponse> students = studentService.getAllNonActiveStudents();
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<StudentResponse>> getAllStudentsInGroup(@PathVariable UUID id){
        List<StudentResponse> allStudents = studentService.getAllStudentsWithGroupdId(id);
        return ResponseEntity.status(HttpStatus.OK).body(allStudents);
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<StudentResponse>> getAllStudenWithLanguage(@PathVariable Language language){
        List<StudentResponse> allStudents = studentService.getAllStudentsWithLanguage(language);
        return ResponseEntity.status(HttpStatus.OK).body(allStudents);
    }

}
