package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.dto.SessionRequest;
import Rastoder.SchoolManagementSystem.dto.SessionResponse;
import Rastoder.SchoolManagementSystem.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final SessionService sessionService;


    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody SessionRequest request){
        SessionResponse response = sessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAllSessions(){
        List<SessionResponse> listOfAllSessions = sessionService.getListOfAllSession();
        return ResponseEntity.status(HttpStatus.OK).body(listOfAllSessions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionResponse> updateSessionDescription(@PathVariable UUID id ,
                                                                    @RequestBody SessionRequest request){
        SessionResponse response =  sessionService.updateSessionDescription(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
