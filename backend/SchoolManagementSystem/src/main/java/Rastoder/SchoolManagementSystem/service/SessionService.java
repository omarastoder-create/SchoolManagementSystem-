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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<SessionResponse> getListOfAllSession() {
        List<Session> listOfSession = sessionRepository.findAll();
        return  listOfSession.stream().map(session ->
                new SessionResponse(
                        session.getSessionId(),
                        session.getStartDate(),
                        session.getContent(),
                        session.getStudentGroup() != null ? session.getStudentGroup().getGroupId() : null
                )).collect(Collectors.toList());
    }
}
