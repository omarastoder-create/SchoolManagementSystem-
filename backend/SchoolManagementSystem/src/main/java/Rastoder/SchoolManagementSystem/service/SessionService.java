package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.SessionRequest;
import Rastoder.SchoolManagementSystem.dto.SessionResponse;
import Rastoder.SchoolManagementSystem.model.Session;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.repository.SessionRepository;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final StudentGroupRepository studentGroupRepository;

    public SessionService(SessionRepository sessionRepository, StudentGroupRepository studentGroupRepository) {
        this.sessionRepository = sessionRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    public SessionResponse createSession(SessionRequest request)throws EntityNotFoundException {

        StudentGroup sg = studentGroupRepository
                .findById(request.studentGroup()).orElseThrow(EntityNotFoundException::new);

        Session session = Session.builder()
                .startDate(LocalDateTime.now())
                .content(request.content())
                .studentGroup(sg)
                .build();

        Session newSession = sessionRepository.save(session);

        return new SessionResponse(
                newSession.getSessionId(),
                newSession.getStartDate(),
                newSession.getContent(),
                newSession.getStudentGroup().getGroupId()
        );
    }
}
