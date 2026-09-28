package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.SessionRequest;
import Rastoder.SchoolManagementSystem.dto.SessionResponse;
import Rastoder.SchoolManagementSystem.dto.StudentResponse;
import Rastoder.SchoolManagementSystem.model.Session;
import Rastoder.SchoolManagementSystem.model.StudentGroup;
import Rastoder.SchoolManagementSystem.repository.SessionRepository;
import Rastoder.SchoolManagementSystem.repository.StudentGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private StudentGroupRepository studentGroupRepository;

    @InjectMocks
    private SessionService underTest;

    @Captor
    private ArgumentCaptor<Session> sessionCaptor;

    private UUID sessionId;
    private UUID studentGroupId;
    private StudentGroup studentGroup;
    private Session dummySession;

    @BeforeEach
    void setUp() {
        sessionId = UUID.randomUUID();
        studentGroupId = UUID.randomUUID();

        studentGroup = new StudentGroup();
        studentGroup.setGroupId(studentGroupId);

        dummySession = new Session(
                sessionId,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                "Learning",
                studentGroup
        );
    }

    @Test
    void createSession_shouldReturnCreatedSession() {
        // Arrange
        SessionRequest request = new SessionRequest("Learning", studentGroupId);

        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.of(studentGroup));
        when(sessionRepository.save(any(Session.class))).thenReturn(dummySession);

        // Act
        SessionResponse response = underTest.createSession(request);

        // Assert
        verify(studentGroupRepository).findById(studentGroupId);
        verify(sessionRepository).save(sessionCaptor.capture());
        Session capturedSession = sessionCaptor.getValue();

        // 1. Asserts für das Objekt VOR dem Speichern in der DB (capturedSession)
        assertThat(capturedSession.getSessionId()).isNull();
        assertThat(capturedSession.getContent()).isEqualTo(request.content());
        assertThat(capturedSession.getStudentGroup()).isEqualTo(studentGroup);
        assertThat(capturedSession.getStartDate()).isNotNull();

        // 2. Asserts für das DTO NACH dem Speichern in der DB (response vs. dummySession)
        assertThat(response).isNotNull();
        assertThat(response.sessionId()).isEqualTo(dummySession.getSessionId());
        assertThat(response.content()).isEqualTo(dummySession.getContent());
        assertThat(response.startDate()).isEqualTo(dummySession.getStartDate());
        assertThat(response.studentGroup()).isEqualTo(studentGroupId);
    }

    @Test
    void getListOfAllSessions_shouldReturnAllSessions() {
        // Arrange
        Session sessionWithoutGroup = new Session(
                UUID.randomUUID(),
                LocalDateTime.of(2024, 1, 2, 10, 0),
                "Self-Study",
                null
        );
        when(sessionRepository.findAll()).thenReturn(List.of(dummySession, sessionWithoutGroup));

        SessionResponse expectedFirst = new SessionResponse(
                dummySession.getSessionId(),
                dummySession.getStartDate(),
                dummySession.getContent(),
                studentGroupId
        );
        SessionResponse expectedSecond = new SessionResponse(
                sessionWithoutGroup.getSessionId(),
                sessionWithoutGroup.getStartDate(),
                sessionWithoutGroup.getContent(),
                null
        );

        // Act
        List<SessionResponse> responses = underTest.getListOfAllSession();

        // Assert
        verify(sessionRepository).findAll();
        assertThat(responses)
                .hasSize(2)
                .containsExactly(expectedFirst, expectedSecond);
    }

    @Test
    void updateSessionDescription_shouldReturnUpdatedSessionDescription() {
        // Arrange
        LocalDateTime originalStartDate = dummySession.getStartDate();
        SessionRequest request = new SessionRequest("Learning 2", studentGroupId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(dummySession));
        when(sessionRepository.save(any(Session.class))).thenReturn(dummySession);

        // Act
        SessionResponse response = underTest.updateSessionDescription(sessionId, request);

        // Assert
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(sessionCaptor.capture());
        Session capturedSession = sessionCaptor.getValue();

        // 1. Asserts before databasemapping
        assertThat(capturedSession.getSessionId()).isEqualTo(sessionId);
        assertThat(capturedSession.getContent()).isEqualTo("Learning 2");
        assertThat(capturedSession.getStartDate()).isEqualTo(originalStartDate);
        assertThat(capturedSession.getStudentGroup()).isEqualTo(studentGroup);

        // 2. Asserts dto communication
        assertThat(response).isNotNull();
        assertThat(response.sessionId()).isEqualTo(sessionId);
        assertThat(response.content()).isEqualTo("Learning 2");
        assertThat(response.startDate()).isEqualTo(originalStartDate);
        assertThat(response.studentGroup()).isEqualTo(studentGroupId);
    }
}
