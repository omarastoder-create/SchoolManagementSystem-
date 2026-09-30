package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.SessionRequest;
import Rastoder.SchoolManagementSystem.dto.SessionResponse;
import Rastoder.SchoolManagementSystem.exception.BusinessRuleViolationException;
import Rastoder.SchoolManagementSystem.model.Session;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.repository.SessionRepository;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final StudentGroupRepository studentGroupRepository;

    public SessionService(SessionRepository sessionRepository, StudentGroupRepository studentGroupRepository) {
        this.sessionRepository = sessionRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @Transactional
    public SessionResponse createSession(SessionRequest request) {
        if (request.content() == null || request.content().isBlank()) {
            throw new BusinessRuleViolationException("Session content must not be blank");
        }

        StudentGroup sg = studentGroupRepository.findById(request.studentGroup())
                .orElseThrow(() -> new EntityNotFoundException(
                        "StudentGroup not found with id: " + request.studentGroup()
                ));

        if (sg.getTeacher() == null) {
            throw new BusinessRuleViolationException("Cannot create a session for a group without an assigned teacher");
        }
        if (sg.getStudents() == null || sg.getStudents().isEmpty()) {
            throw new BusinessRuleViolationException("Cannot create a session for an empty student group");
        }

        Session session = Session.builder()
                .startDate(LocalDateTime.now())
                .content(request.content())
                .studentGroup(sg)
                .build();

        Session newSession = sessionRepository.save(session);
        return getSessionResponse(newSession);
    }

    public List<SessionResponse> getListOfAllSession() {
        return sessionRepository.findAll()
                .stream()
                .map(this::getSessionResponse)
                .toList();
    }

    @Transactional
    public SessionResponse updateSessionDescription(UUID id, SessionRequest request) {
        if (request.content() == null || request.content().isBlank()) {
            throw new BusinessRuleViolationException("Session content must not be blank");
        }

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));

        session.setContent(request.content());
        Session savedSession = sessionRepository.save(session);

        return getSessionResponse(savedSession);
    }

    private SessionResponse getSessionResponse(Session session) {
        return new SessionResponse(
                session.getSessionId(),
                session.getStartDate(),
                session.getContent(),
                session.getStudentGroup() != null ? session.getStudentGroup().getGroupId() : null
        );
    }
}