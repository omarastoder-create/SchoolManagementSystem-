package Rastoder.SchoolManagementSystem.Controller;

import Rastoder.SchoolManagementSystem.dto.TeacherRequest;
import Rastoder.SchoolManagementSystem.dto.TeacherResponse;
import Rastoder.SchoolManagementSystem.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @PostMapping
    public ResponseEntity<TeacherResponse> addTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        // 1. Pass the validated JSON payload to the Service Layer and capture the result
        TeacherResponse response = teacherService.createTeacher(teacherRequest);
        // 2. Wrap the DTO in a 201 Created HTTP status and return to the client
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getAllTeachers(){
        List<TeacherResponse> responses = teacherService.getAllTeachers();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable UUID id){
        TeacherResponse t = teacherService.getTeacherById(id);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacherWithId
            (@PathVariable UUID id , @RequestBody TeacherRequest request){
        TeacherResponse t = teacherService.updateTeacherWithId(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }

}
