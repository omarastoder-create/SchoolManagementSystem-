package Rastoder.SchoolManagementSystem.Controller;

import Rastoder.SchoolManagementSystem.dto.ParentRequest;
import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.service.ParentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parents")
public class ParentController {

    private final ParentService parentService;


    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }
        @PostMapping
    public ResponseEntity<ParentResponse> createParent(@RequestBody @Valid ParentRequest request){
        ParentResponse response = parentService.createParent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentResponse> getParentById(@PathVariable UUID id){
        ParentResponse response = parentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentResponse> updateParentWithId(
            @PathVariable UUID id , @RequestBody ParentRequest request){
        ParentResponse response = parentService.updateParentWithId(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
