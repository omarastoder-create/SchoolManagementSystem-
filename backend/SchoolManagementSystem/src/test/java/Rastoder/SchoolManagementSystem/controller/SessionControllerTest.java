package Rastoder.SchoolManagementSystem.controller;

import Rastoder.SchoolManagementSystem.Controller.SessionController;
import Rastoder.SchoolManagementSystem.dto.SessionRequest;
import Rastoder.SchoolManagementSystem.dto.SessionResponse;
import Rastoder.SchoolManagementSystem.service.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @MockitoBean
    private SessionService sessionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSessions() throws Exception {
        // 1. ARRANGE
        UUID studentGroupId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        SessionRequest mockRequest = new SessionRequest(
                "Learning the way of life",
                studentGroupId
        );

        SessionResponse mockResponse = new SessionResponse(
                sessionId,
                LocalDateTime.of(2026, 9, 24, 10, 0),
                "Learning the way of life",
                studentGroupId
        );

        // Wir mocken die Abhängigkeit (den Service), NICHT das System Under Test (den Controller)
        Mockito.when(sessionService.createSession(Mockito.any(SessionRequest.class)))
                .thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Learning the way of life"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").value(sessionId.toString()));
    }

    @Test
    void getSessions() throws Exception{
        UUID studentGroupId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        SessionResponse mockResponse = new SessionResponse(
                sessionId,
                LocalDateTime.of(2026, 9, 24, 10, 0),
                "Learning the way of life",
                studentGroupId
        );

        Mockito.when(sessionService.getListOfAllSession()).thenReturn(List.of(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sessions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Learning the way of life"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sessionId").value(sessionId.toString()));
    }

    @Test
    void updateSessions() throws Exception{
        UUID studentGroupId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        SessionRequest mockRequest = new SessionRequest(
                "Learning the way of life",
                studentGroupId
        );

        SessionResponse mockResponse = new SessionResponse(
                sessionId,
                LocalDateTime.of(2026, 9, 24, 10, 0),
                "Learning the way of life",
                studentGroupId
        );

        Mockito.when(sessionService.updateSessionDescription(sessionId, mockRequest))
                .thenReturn(mockResponse);

        // 2. ACT & ASSERT
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/sessions/"+sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Learning the way of life"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").value(sessionId.toString()));

    }
}
