package Rastoder.SchoolManagementSystem.controller;


import Rastoder.SchoolManagementSystem.dto.StudentGroupRequest;
import Rastoder.SchoolManagementSystem.dto.StudentGroupResponse;
import Rastoder.SchoolManagementSystem.service.StudentGroupService;
import jakarta.validation.Valid;
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

    @PutMapping("{groupId}/student/{studentId}")
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

    @GetMapping("/{id}")
    public ResponseEntity<StudentGroupResponse> getStudentGroupById(@PathVariable UUID id){
        StudentGroupResponse response = studentGroupService.getStudentGroupByService(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Controller Layer
    @PutMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<StudentGroupResponse> assignNewTeacher(
            @PathVariable UUID groupId,
            @PathVariable UUID teacherId) {
        StudentGroupResponse response = studentGroupService.assignTeacher(groupId, teacherId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<StudentGroupResponse>> getAllStudentGroupsForTeacher(
            @PathVariable UUID id){
        List<StudentGroupResponse> listOfStudentGroups = studentGroupService.getStudentGroupByTeacherId(id);
        return ResponseEntity.status(HttpStatus.OK).body(listOfStudentGroups);
    }






}
