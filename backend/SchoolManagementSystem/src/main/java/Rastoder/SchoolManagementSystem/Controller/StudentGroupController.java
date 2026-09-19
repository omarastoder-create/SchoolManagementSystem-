package Rastoder.SchoolManagementSystem.Controller;


import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.model.Student;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.service.StudentGroupService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/studentGroups")
public class StudentGroupController {

    private final StudentGroupService studentGroupService;


    public StudentGroupController(StudentGroupService studentGroupService) {
        this.studentGroupService = studentGroupService;
    }

    @PostMapping
    public ResponseEntity<StudentGroupResponse> createStudentGroup(@Valid @RequestBody StudentGroupRequest request){
        StudentGroupResponse response = studentGroupService.createStudentGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{groupId}/{studentId}")
    public ResponseEntity<Void> addStudentToGroup(@PathVariable UUID groupId,
                                                  @PathVariable UUID studentId){
        studentGroupService.addStudentToStudentGroup(studentId,groupId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<StudentGroupResponse>> getAllStudents(){
        List<StudentGroupResponse> listOfSG = studentGroupService.getAllStudentGroups();
        return ResponseEntity.status(HttpStatus.OK).body(listOfSG);
    }
}
